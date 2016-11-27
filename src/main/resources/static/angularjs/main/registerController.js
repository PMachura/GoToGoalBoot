/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//zmienic nazwe na user controller 
angular.module("goToGoalModule")
        .controller("registerController", function ($scope, userResourceHandler) {
                $scope.datePattern = new RegExp("^\\d{4}-([0][1-9]|[1][0-2])-[0-3][0-9]$");
                $scope.currentUser = {
                  };      
                  
                $scope.createUser = function(user){
                    
                    userResourceHandler.createUser(user).$promise.then(function(userResponse){
                        $scope.message = "Twoje nowe konto zostało utworzone";
                    })
                }
            });
        