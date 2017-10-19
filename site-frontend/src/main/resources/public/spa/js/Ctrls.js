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
        var result = AppService.getUser()? true:false;
        return result;
    };
    
    //from UI
    $scope.cleanLoginError = function(){
        $scope.loginError = null;
    };
    
    $scope.cleanCommonMessage = function(){
        $scope.commonMessage = "";
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

    //event
    
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

    //search for the newest CMS and go
    CmsService.cms.doRecordSearch({})
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("indexMainCtrl Search success", result);
                var content = result.data.data.content;
                if(content && content.length > 0){
                    $state.go('cmsRecord', { id : content[0].id }, {reload: true});
                }
            }else{
                $rootScope.$emit(Consts.Event.Message, "indexMainCtrl Search:" + Utils.getResponseMessage(result));
            }
        }, function(result){
            $log.warn("indexMainCtrl Search fail", result);
            $rootScope.$emit(Consts.Event.Message, "onSearch:" + Utils.getResponseMessage(result));
        });
});


app.controller('cmsMainCtrl', function($rootScope, $scope, $state, $stateParams, $log, $timeout, screenSize, CmsService, AppService, $) {
    //UI
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

    $scope.onLoadRecord = function (record) {
        $log.info("Load Record Page:", record);
        $state.go('cmsRecord', { id : record.id }, {reload: true});
    };
    
    //on event
    
    //init

});

app.controller('cmsRecordCtrl', function($rootScope, $state, $scope, $stateParams, $log, $timeout, screenSize, CmsService, AppService, $, CK) {
    
    //UI
    $scope.input = {};//newTag:
    $scope.canEdit = false;
    //data
    $scope.record = AppService.getRecord();

    $scope.recordDeleteError = "";
    $scope.isDeleting = false;

    $scope.htmlEditor = "";
    $scope.imagePath = "";

    $scope.hasRecord = function () {
        if(AppService.getRecord() && AppService.getRecord().id){
            return true;
        }else{
            return false;
        }
    };

    $scope.onSave = function(){
        $log.info("onSave:", $scope.record);

        if(!AppService.getUser()){
            $rootScope.$emit(Consts.Event.Message, "Not Login Yet");
            return ;
        }

        //set userinfo if empty
        if(!$scope.record.userId){
            $scope.record.userId = AppService.getUser().id;
            $scope.record.username = AppService.getUser().username;
        }

        //get data
        $scope.record.content = $scope.editor.getData();
        
        if($scope.input.isPrivate){
            $scope.record.visibility = Consts.Record.Private;
        }else{
            $scope.record.visibility = Consts.Record.Public;
        }
        $scope.record.recordType = Consts.Record.Article;
        
        CmsService.cms.doRecordUpdate($scope.record)
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("onSave success", result);
                $rootScope.$emit(Consts.Event.Message, "onSave Success", 3000, Consts.MessageLevel.Fine);
                AppService.setRecord(result.data.data);
                //set state
                $state.go('cmsRecord', { id : result.data.data.id }, {reload: true});
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

    $scope.onPopDelete = function(){
        $scope.recordDeleteError = "";
    };

    $scope.onDelete = function(){
        $log.info("Delete:" + AppService.getRecord().id);
        //reset ui var
        $scope.isDeleting = true;
        $scope.loginError = "";

        CmsService.cms.doRecordDelete(AppService.getRecord().id)
            .then(function(result){
                if(result.data && result.data.status == Consts.ResponseStatus.OK){
                    $log.info("onDelete success", result);
                    //AppService.setRecord(null);
                    $state.go("cmsMain", {}, {reload: true});
                    //close modal
                    $rootScope.$emit(Consts.Event.Message, "Delete Success", 3000, Consts.MessageLevel.Fine);
                    $('#recordDeleteModal').modal('hide');
                }else{
                    $scope.recordDeleteError = Utils.getResponseMessage(result);
                }
            },function(result){
                $log.warn("Delete fail", result);
                $scope.loginError = Utils.getResponseMessage(result);
            }).finally(function(){
            $scope.isDeleting = false;
        });
    };

    var setupEditor = function(canEdit, setContent){
        if(!$scope.editor){
            $scope.editor = CK.inline("contentEditor", {
                filebrowserBrowseUrl: CmsService.cms.getFileBrowserPath(),
                filebrowserUploadUrl: CmsService.cms.getFileUploadPath()
            });
            $scope.editor.setData(setContent);
            $scope.$on("$destroy", function () {
                if($scope.editor){
                    $scope.editor.destroy();
                }
            });
        }

        CK.on( 'instanceReady', function( evt ) {
            if(canEdit){
                evt.editor.setReadOnly(false);
            }else{
                evt.editor.setReadOnly(true);
            }
        });
    };

    var checkCanEditAndEditor = function(){
        var record = AppService.getRecord();
        var currentUser = AppService.getUser();

        if(record){
            if(currentUser && (currentUser.role == Consts.Role.Admin ||  currentUser.id == record.userId || !record.id )){
                $scope.canEdit = true;
            }else{
                $scope.canEdit = false;
            }
            setupEditor($scope.canEdit, record.content);
        }
    };

    //on event
    var userChange = $rootScope.$on(Consts.Event.UserChange, function(e, userInfo){
        $log.info("get on " + Consts.Event.UserChange, userInfo);
        checkCanEditAndEditor();
    });
    $scope.$on("$destroy", userChange);

    var removeRecord = $rootScope.$on(Consts.Event.RecordLoaded, function(e, newRecord){
        $log.info("get on " + Consts.Event.RecordLoaded, newRecord);
        $scope.record = newRecord;
        if(newRecord.recordType == "File"){
            $scope.imagePath = Consts.ImageRoot + newRecord.blobId;
        }else{
            checkCanEditAndEditor();
        }
    });
    $scope.$on("$destroy", removeRecord);

    
    //init

    if($stateParams.id){
        $log.info("load record:" + $stateParams.id);
        CmsService.cms.doRecordGet($stateParams.id)
        .then(function(result){
            if(result.data && result.data.status == Consts.ResponseStatus.OK){
                $log.info("load record success", result);
                var record = result.data.data;
                AppService.setRecord(record);
            }else{
                $rootScope.$emit(Consts.Event.Message, "load record:" + Utils.getResponseMessage(result));
            }
        }, function(result){
            $log.warn("load record fail", result);
            $rootScope.$emit(Consts.Event.Message, "load record:" + Utils.getResponseMessage(result));
        });
    }else{
        if(!AppService.getRecord()){
            AppService.setRecord(new Record());
        }
        checkCanEditAndEditor();
    }


    $log.info("Record Page Loaded", $stateParams);
});

