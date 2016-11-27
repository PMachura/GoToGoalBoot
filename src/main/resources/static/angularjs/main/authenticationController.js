/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("goToGoalModule")
        .constant("userPrincipalUrl", "http://localhost:8080/api/users/principal")
        .constant("loggedUserDataUtl", "http://localhost:8080/api/users/logged")
        .constant("logoutUrl", "http://localhost:8080/logout")
        .controller("authenticationController", function ($rootScope, $scope, $http, $location, userPrincipalUrl, logoutUrl, loggedUserDataUtl) {

            
            var getLoggedUser = function(){
                $http.get(loggedUserDataUtl).then(function(response){
                    $rootScope.user = response.data;
                }, function(){
                    $rootScope.messageHandler.set("Wystąpiły problemy podczas autoryzacji, spróbuj ponownie");
                });
            };

            var authenticate = function (credentials, callback) {

                var headers = credentials ? {authorization: "Basic "
                            + btoa(credentials.userEmail + ":" + credentials.password)
                } : {};

                $http.get(userPrincipalUrl, {headers: headers}).then(function (response) {
                    if (response.data.name) {
                        $rootScope.authenticated = true;
                    } else {
                        $rootScope.authenticated = false;
                    }
                    callback && callback();
                }, function () {
                    $rootScope.authenticated = false;
                    callback && callback();
                });

            };

            authenticate();
            if($rootScope.authenticated){
                getLoggedUser();
            }
            
            $scope.credentials = {};
            $scope.login = function () {
                authenticate($scope.credentials, function () {
                    if ($rootScope.authenticated) {
                        $rootScope.messageHandler.hide();
                        getLoggedUser();
                        $location.path("");
                    } else {
                        $rootScope.messageHandler.set("Niepoprawny login lub hasło");
                        $location.path("");
                    }
                });
            };

            $scope.logout = function () {
                $http.post(logoutUrl, {}).finally(function () {
                    $rootScope.user = {};
                    $rootScope.authenticated = false;
                    $location.path("");
                });
            };
        });