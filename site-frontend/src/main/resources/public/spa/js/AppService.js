
app.factory("AppService", function($rootScope){
    var config = {
        
    };
    
    var data = {
        user:null,
        record:null,
        searchParam: new Record()
    };
    
    var service = {
        getUser : function(){return data.user},
        setUser : function(userInfo){
            data.user = userInfo;
            $rootScope.$emit(Consts.Event.UserChange, data.user);
        },
        getRecord : function(){return data.record},
        setRecord : function(recordInfo){
            data.record = recordInfo;
            $rootScope.$emit(Consts.Event.RecordLoaded, data.record);
        },
        getSearchParam : function(){
            return data.searchParam;
        }
    };
    
    return service;

});