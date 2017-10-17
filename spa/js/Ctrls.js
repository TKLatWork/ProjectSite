//主控制器，界面的自适应，等
app.controller('mainCtrl', function($rootScope, $scope, $state, $log, $timeout, CmsService, AppService, $) {
    
    //UI control
    $scope.inUsername = "";
    $scope.inPassword = "";
    
    
    $scope.commonMessage = "";
    $scope.commonMessageLevel = Consts.MessageLevel.Alert;
    $scope.loginError = "";
    $scope.isLogining = false;

    
    $scope.isFineMessage = function(){
        return $scope.commonMessageLevel == Consts.MessageLevel.Fine;
    };
    $scope.isAlertMessage = function(){
        return $scope.commonMessageLevel == Consts.MessageLevel.Alert;
    };
    $scope.isWarnMessage = function(){
        return $scope.commonMessageLevel == Consts.MessageLevel.Warn;
    };
    
    $scope.displayUsername = function(){
        return $scope.hasLogin() ? AppService.getUser().username : "Visitor";
    };

    $scope.hasLogin = function(){
        return AppService.getUser()? true:false;
    };
    
    //from UI
    $scope.cleanLoginError = function(){
        $scope.loginError = null;
    };
    
    $scope.cleanCommonMessage = function(){
        $scope.commonMessage = "";
    };
    
    $scope.onSwitchRight = function(){
        $rootScope.$emit(Consts.Event.SwitchRight);  
    };
    
    //clean pop data
    $scope.onPopLogin = function(){
        $scope.loginError = "";
        $scope.inUsername = "tkl";
        $scope.inPassword = "123";
    };
    
    $scope.onCurrent = function(){
        CmsService.user.getCurrent()
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("onCurrent success", result);
                AppService.setUser(result.data.data);
            }else{
                $log.info("onCurrent fail" + Utils.getResponseMessage(result));
                AppService.setUser(null);
            }
        },function(result){
            $log.info("onCurrent fail:" + Utils.getResponseMessage(result));
            AppService.setUser(null);
        });
    };
    
    $scope.onLogin = function(){
        $log.info("onLogin:" + $scope.inUsername + "/" + $scope.inPassword);
        //reset ui var
        $scope.isLogining = true;
        $scope.loginError = "";
        
        CmsService.user.doLogin($scope.inUsername, $scope.inPassword)
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("login success", result);
                AppService.setUser(result.data.data);
                //close modal
                $rootScope.$emit(Consts.Event.Message, "Login Success", 3000, Consts.MessageLevel.Fine);
                $('#loginModal').modal('hide');
            }else{
                $scope.loginError = Utils.getResponseMessage(result);
            }
        },function(result){
            $log.warn("login fail", result);
            $scope.loginError = Utils.getResponseMessage(result);
        }).finally(function(){
            $scope.isLogining = false;
        });
    };
    
    $scope.onLogout = function(){
        $log.info("onLogout:");
        CmsService.user.doLogout()
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("onLogout success", result);
                $rootScope.$emit(Consts.Event.Message, "Logout Success", 3000, Consts.MessageLevel.Fine);
                AppService.setUser(null);
            }else{
                $rootScope.$emit(Consts.Event.Message, "onLogout:" + Utils.getResponseMessage(result));
            }
        },function(result){
            $log.warn("onLogout fail", result);
            $rootScope.$emit(Consts.Event.Message, "onLogout:" + Utils.getResponseMessage(result));
        });
    };
    
    //sub ctrl control
    $scope.onLoadCms = function(){
        $state.go('cmsMain', {}, {reload: true});
    };
    
    $scope.onNewRecord = function(){
        $state.go('cmsRecord', {}, {reload: true});
    };
    
    $scope.onLoadIndex = function(){
        $state.go('index', {}, {reload: true});
    };
    
    //etc
    var removeMe = $rootScope.$on(Consts.Event.Message, function(event, message, timeout, level){
        $log.info("get on " + Consts.Event.Message + ":" + message);
        $scope.commonMessage = message;
        //default
        if(!level){
            level = Consts.MessageLevel.Alert;
        }
        $scope.commonMessageLevel = level;
        //if auto clean
        if(timeout){
            $timeout(function(){$scope.cleanCommonMessage();}, timeout);
        }
    });
    $scope.$on("$destroy", removeMe);
    
    
    //init
    if(!AppService.getUser()){
        $scope.onCurrent();
    }
});


//子controller控制部分内容，并切换
app.controller('indexMainCtrl', function($rootScope, $scope, $state, $log, $timeout, CmsService, AppService, $) {
});


app.controller('cmsMainCtrl', function($rootScope, $scope, $stateParams, $log, $timeout, screenSize, CmsService, AppService, $) {    
    //UI
    $scope.ifShowRight = null;
    //data
    $scope.query = AppService.getSearchParam();
    $scope.searchResult = null;
    
    $scope.onSearch = function(){
        $log.info("onSearch", AppService.getSearchParam());
        CmsService.cms.doRecordSearch(AppService.getSearchParam())
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("onSearch success", result);
                $rootScope.$emit(Consts.Event.Message, "onSearch Success", 3000, Consts.MessageLevel.Fine);
                $scope.searchResult = result.data.data;
            }else{
                $rootScope.$emit(Consts.Event.Message, "onSearch:" + Utils.getResponseMessage(result));
            }
        }, function(result){
            $log.warn("onSearch fail", result);
            $rootScope.$emit(Consts.Event.Message, "onSearch:" + Utils.getResponseMessage(result));
        });
    };
    
    $scope.onMovePage = function(offset){
        
    };
    
    //on event
    var removeMe = $rootScope.$on(Consts.Event.SwitchRight, function(){
        $log.info("get on " + Consts.Event.SwitchRight);
        if($scope.ifShowRight){
            $scope.ifShowRight = false;
        }else{
            $scope.ifShowRight = true;
        }
    });
    $scope.$on("$destroy", removeMe);
    
    //init
    if($scope.ifShowRight === null){
        if(screenSize.is('xs')){
            $scope.ifShowRight = false;
        }else{
            $scope.ifShowRight = false;
        }
    }

});

app.controller('cmsRecordCtrl', function($rootScope, $scope, $stateParams, $log, $timeout, screenSize, CmsService, AppService, $) {
    
    //UI
    $scope.ifShowRight = null;
    $scope.input = {};//newTag:
    //data
    $scope.record = AppService.getRecord();   

    $scope.onSave = function(){
        $log.info("onSave:", $scope.record);
        //set userinfo if empty
        if(!$scope.record.userId){
            $scope.record.userId = AppService.getUser().id;
            $scope.record.username = AppService.getUser().username;
        }
        
        if($scope.input.isPrivate){
            $scope.record.visibility = Consts.Record.Private;
        }else{
            $scope.record.visibility = Consts.Record.Public;
        }
        
        if($scope.input.isFile){
            $scope.record.recordType = Consts.Record.File;
        }else{
            $scope.record.recordType = Consts.Record.Article;
        }
        
        CmsService.cms.doRecordUpdate($scope.record)
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("onSave success", result);
                $rootScope.$emit(Consts.Event.Message, "onSave Success", 3000, Consts.MessageLevel.Fine);
                AppService.setRecord(result.data.data);
            }else{
                $rootScope.$emit(Consts.Event.Message, "onSave:" + Utils.getResponseMessage(result));
            }
        }, function(result){
            $log.warn("onSave fail", result);
            $rootScope.$emit(Consts.Event.Message, "onSave:" + Utils.getResponseMessage(result));
        });
    };
    
    $scope.onAddTag = function(){
        $log.info("onAddTag:" + $scope.input.newTag, $scope.record.tags);
        if($scope.input.newTag){
            if($scope.record.tags.indexOf($scope.input.newTag) === -1){
                $scope.record.tags.push($scope.input.newTag);
            }
        }
    };
    
    $scope.onRemoveTag = function(){
        $log.info("onRemoveTag:" + $scope.input.newTag, $scope.record.tags);
        if($scope.input.newTag){
            var index = $scope.record.tags.indexOf($scope.input.newTag);
            if(index !== -1){
                $scope.record.tags.splice(index, 1);
            }
        }
    };
    
    
    //on event
    var removeSwitch = $rootScope.$on(Consts.Event.SwitchRight, function(){
        $log.info("get on " + Consts.Event.SwitchRight);
        if($scope.ifShowRight){
            $scope.ifShowRight = false;
        }else{
            $scope.ifShowRight = true;
        }
    });
    $scope.$on("$destroy", removeSwitch);
    
    var removeRecord = $rootScope.$on(Consts.Event.RecordLoaded, function(e, newRecord){
        $log.info("get on " + Consts.Event.RecordLoaded, newRecord);
        $scope.record = newRecord;
    });
    $scope.$on("$destroy", removeRecord);
    
    //init
    if($scope.ifShowRight === null){
        if(screenSize.is('xs')){
            $scope.ifShowRight = false;
        }else{
            $scope.ifShowRight = false;
        }
    }
    
    if(!AppService.getRecord()){
        AppService.setRecord(new Record());
    }
    
});

