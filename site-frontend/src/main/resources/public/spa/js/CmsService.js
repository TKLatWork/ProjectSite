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
            fileBrowser : "/cms/private/fileBrowser",
            fileGet : "/cms/public/file/",
            fileUpdate : "/cms/private/file",
        },
        post : function (url, data) {
            return $http({
                method: 'POST',
                url: url,
                data: data,
                withCredentials: true
            });
        },
        get : function (url, data) {
            return $http({
                method: 'GET',
                url: url,
                data: data,
                withCredentials: true
            });
        },
        delete : function (url, data) {
            return $http({
                method: 'DELETE',
                url: url,
                data: data,
                withCredentials: true,
                headers: {'Content-Type': 'application/json;charset=UTF-8'}
            });
        }
    };
    
    
    var service = {
        user:{
            getCurrent:function(){
                return config.get(config.root + config.user.current);
            },
            doLogin:function(username, password){
                return $http({
                    method: 'POST',
                    url: config.root + config.user.login,
                    data: $.param({
                        username : username,
                        password : password
                    }),
                    withCredentials: true,
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'}
                }); 
            },
            doLogout:function(){
                return config.get(config.root + config.user.logout);
            }
        },
        cms:{
            doRecordSearch:function(queryRecord){
                return config.post(config.root + config.cms.recordSearch, queryRecord);
            },
            doRecordGet:function(recordId){
                return config.post(config.root + config.cms.recordGet, {
                    id : recordId
                });
            },
            doRecordUpdate:function(record){
                return config.post(config.root + config.cms.recordUpdate, record);
            },
            doRecordDelete:function(recordId){
                return config.delete(config.root + config.cms.recordDelete, {
                    id : recordId
                });
            },
            getFileBrowserPath:function(){
                return config.root + config.cms.fileBrowser;
            },
            getFilePath:function (fileName) {
                return config.root + config.cms.fileGet;
            },
            getFileUploadPath:function (fileName) {
                return config.root + config.cms.fileUpdate;
            }
            //TODO function for file (path)
        }
    };
    
    return service;

});