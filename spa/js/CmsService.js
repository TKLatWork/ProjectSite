app.factory("CmsService", function($http, $q, $){
    var config = {
        root : "http://localhost/site",
        user:{
            current : "/user/public/current",
            login : "/user/public/login",
            logout : "/user/public/logout",
        },
        cms:{
            recordSearch : "/cms/public/recordSearch",
            recordGet : "/cms/public/record",
            recordUpdate : "/cms/private/record",
            recordDelete : "/cms/private/record",
            fileGet : "/cms/public/file",
            fileUpdate : "/cms/private/file",
        }
    };
    
    
    var service = {
        user:{
            getCurrent:function(){
                return $http.get(config.root + config.user.current);
            },
            doLogin:function(username, password){
                return $http({
                    method: 'POST',
                    url: config.root + config.user.login,
                    data: $.param({
                        username : username,
                        password : password
                    }),
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                });
            },
            doLogout:function(){
                return $http.get(config.root + config.user.logout);
            }
        },
        cms:{
            doRecordSearch:function(queryRecord){
                return $http.post(config.root + config.cms.recordSearch, queryRecord);
            },
            doRecordGet:function(recordId){
                return $http.get(config.root + config.cms.recordGet, {
                    id : recordId
                });
            },
            doRecordUpdate:function(record){
                return $http.post(config.root + config.cms.recordUpdate, record);
            },
            doRecordDelete:function(recordId){
                return $http.post(config.root + config.cms.recordDelete, {
                    id : recordId
                });
            }
            //TODO function for file (path)
        }
    };
    
    return service;

});