/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//zmienic nazwe na user controller 
angular.module("goToGoalModule")
        .controller("userController", function ($scope, userResourceHandler, $rootScope, $location, authenticationHandler) {

            $scope.currentUser = {};           
            $rootScope.alerter.setDefault();

            authenticationHandler.getLoggedUserRequest().then(function (response) {
                $rootScope.user = response.data;
                $rootScope.displayMode.setView();
            }, function(error){
                
            });
           

            $scope.createOrEditUser = function (user) {
                if (user) {
                    $scope.currentUser = angular.copy(user);
                } else {
                    $scope.currentUser = {};
                }
                $rootScope.alerter.hide();
                $rootScope.displayMode.setCreator();
            };

            $scope.saveCurrentUser = function (currentUser) {
                console.log(currentUser);
                if (angular.isDefined(currentUser.id)) {
                    $scope.updateUser(currentUser);
                } else {
                    $scope.createUser(currentUser);
                }
            };

            $scope.createUser = function (user) {
                console.log("create");
                userResourceHandler.createUserRequest(user).$promise.then(function (userResponse) {
                    $rootScope.alerter.setSuccessMessage("Twoje konto zostało utworzone, zaloguj się by korzystać z serwisu");
                    $rootScope.displayMode.setView();
                    $location.path("");
                }, function (error) {
                    $rootScope.alerter.setWarnintMessage(error.data.message);
                    $location.path("/register");
                });
            };
            
            $scope.updateUser = function (user) {
                userResourceHandler.updateUserRequest(user).$promise.then(function (userResponse) {
                    $rootScope.user = userResponse;
                    $rootScope.alerter.setSuccessMessage("Twoje konto zostało zaktualizowane");
                    $rootScope.displayMode.setView();
                    $location.path("/user");
                }, function (error) {
                    $rootScope.alerter.setWarnintMessage(error.data.message);
                    $location.path("/user");
                });
            };

        });
        