/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("goToGoalModule")
        .controller("goToGoalController", function ($scope, $rootScope, $location, authenticationHandler) {
            
            $rootScope.datePattern = new RegExp("^\\d{4}-([0][1-9]|[1][0-2])-[0-3][0-9]$");
            $rootScope.timePattern = new RegExp("^([0-1][0-9]|2[0-3]):[0-5][0-9](:[0-5][0-9]|)$");  
            $rootScope.displayMode ={
                creation: false,
                view: true,
                setCreator: function(){
                    this.creation = true;
                    this.view = false;
                },
                setView: function(){
                    this.creation = false;
                    this.view = true;
                }
            };
            $rootScope.alerter = {
                display: false,
                messageText: "",
                class: "alert-info",
                setMessage: function(message){
                    this.display=true;
                    this.messageText=message;
                },
                setInfoMessage: function(message){
                    this.setMessage(message);
                    this.class = "alert-info";
                },
                setSuccessMessage: function(message){
                    this.setMessage(message);
                    this.class="alert-success";
                },
                setWarnintMessage: function(message){
                    this.setMessage(message);
                    this.class="alert-warning";
                },
                hide: function(){
                    this.display = false;
                },
                setDefault: function(){
                    this.display = false;
                    this.messageText= "";
                    this.class= "alert-info";
                }
                
            };
                    
            $scope.credentials = {};

            

            var authenticate = function (credentials, callback) {
                authenticationHandler.loginRequest(credentials).then(function (response) {
                    console.log(response);
                    if (response.data.name) {
                        $rootScope.authenticated = true;
                        authenticationHandler.getLoggedUserRequest().then(function (response) {
                            $rootScope.user = response.data;
                            authenticationHandler.setLoggedUser(response.data);
                        }, function () {
                            $rootScope.authenticated = false;
                            $rootScope.alerter.setWarnintMessage("Wystąpił problem podczas pobierania twojego profilu, spróbuj ponownie");
                        });
                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback();
                }, function () {
                    $rootScope.authenticated = false;
                    $location.path("/register");
                    callback && callback();
                });
            };

            $scope.login = function (credentials) {
                authenticate(credentials, function () {
                    if ($rootScope.authenticated) {
                        $rootScope.alerter.hide();
                        $location.path("");
                    } else {
                        $rootScope.alerter.setInfoMessage("Niepoprawny login lub hasło");                 
                        $location.path("");
                    }
                });
            };

            $scope.logout = function () {
                authenticationHandler.logoutRequest().finally(function () {
                    authenticationHandler.setLoggedUser({});
                    $rootScope.user = {};
                    $rootScope.authenticated = false;
                    $rootScope.alerter.setDefault();
                    $location.path("");
                });
            };

          authenticate();

        });