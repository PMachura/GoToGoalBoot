/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
angular.module("goToGoal")
        .constant("foodProductsDataUrl", "http://localhost:8080/api/foodProducts")
        .controller("goToGoalCtrl", function ($scope, $http, foodProductsDataUrl) {
            $scope.data = {};
            $http.get(foodProductsDataUrl)
                    .success(function (data) {
                        var foodProducts = [];
                        for (var i = 0; i < data.length; i++) {
                            foodProducts.push(data[i].foodProduct);
                        }
                        $scope.data.foodProducts = foodProducts;

                    })
                    .error(function (error) {
                        $scope.data.error = error;
                    });
        });
