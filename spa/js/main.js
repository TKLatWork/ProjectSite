var app = angular.module('webCmsApp', ['ui.router', 'matchMedia']);

app.service('$',function(){return $;});

app.config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');
    
    $stateProvider
    .state('cmsMain', {
        url: "/cms",
        templateUrl: "template/cms-main.html",
        controller: "cmsMainCtrl"
    }).state('cmsRecord', {
        url: "/cms/record",
        templateUrl: "template/cms-record.html",
        controller: "cmsRecordCtrl"
    }).state('index', {
        url: "/",
        templateUrl: "template/index-main.html",
        controller: "indexMainCtrl"
    });
});

//utils
var Utils = {
  getResponseMessage : function(response){
      if(!response){
          return "Response is empty";
      }
      //if is http
      var data = response;
      //Orz
      if(response.data){
          data = response.data;
          if(data.data){
              data = data.data;
          }
      }
      
      var result = null;
      if(data.xhrStatus){
          result = data.xhrStatus + ":" + data.status + ":" + data.statusText;
      }else if(data.status){
          result = data.status + ":" + data.message;
      }else{
          result = data;
      }
      
      return result;
  }
};

var Consts = {
    ResponseStatus : {
        OK : "OK",
        Exception : "Exception",
        Fail : "Fail"
    },
    Event : {
        Message : "Message",
        SwitchRight : "SwitchRight",
        RecordLoaded : "RecordLoaded"
    },
    MessageLevel : {
        Fine : "fine",
        Warn : "Warn",
        Alert : "Alert"
    },
    Record : {
        File : "File",
        Article : "Article",
        Public : "Public",
        Private : "Private"
    }
}


//data class
function Record(){
    this.id = null;
    this.name = null;
    this.category = null;
    this.tags = [];
    this.userId = null;
    this.username = null;
    this.createDate = null;
    this.lastModifyDate = null;
    this.visibility = null;
    this.recordType = null;
    this.content = null;
    this.blobId = null;
};

function UserInfo(){
    this.id = null;
    this.username = null;
    this.password = null;
    this.role = null;
};

function CommonResponse(){
    this.status = null;
    this.message = null;
    this.attachment = null;
    this.data = null;
}