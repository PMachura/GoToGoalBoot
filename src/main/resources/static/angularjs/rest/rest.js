/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// OPCJE - ZMIENIĆ transformRequest ale to jest słabe to trzeba zmieniać wszystko ;p  | ZROBIĆ TAK BY KONKRETNE FOOD PRODUCTY BEZ LINKÓW BYŁY RÓWNIEŻ RESOURCAMI
angular.module("restModule", ["ngResource", "macronutrientsCalculatorModule"])
        .constant("userEmail", "p@p")
        .constant("userId", "1")
        .constant("resourceUrlPrefix", "http://localhost:8080/api")
        .controller("restController", function ($resource, $scope, $http, userEmail, resourceUrlPrefix, macronutrientsCalculator) {


            var mapToResource = function (data, resourceClass, propertyName) {
                if (angular.isDefined(propertyName)) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].hasOwnProperty(propertyName)) {
                            data[i][propertyName] = new resourceClass(data[i][propertyName]);
                        }

                    }
                } else {
                    for (var i = 0; i < data.length; i++) {
                        data[i] = new resourceClass(data[i]);
                    }
                }
                return data;
            };
            $scope.displayMode = {editNutritionDay: false};


            //*********************** FOOD PRODUCTS ****************************//

            $scope.foodProductsResource = $resource(resourceUrlPrefix + "/foodProducts/:id", {id: "@id"});
            $scope.getFoodProducts = function () {
                $scope.foodProductsResource.query(function (data) {
                    $scope.foodProducts = data;
                });
            };
            $scope.foodProductTest = {};
            $scope.created = {};
            $scope.createProduct = function () {
                $scope.created = new $scope.foodProductsResource($scope.foodProductTest);
            };
            $scope.updateExistedProduct = function () {
                $scope.foodProducts[0].foodProduct.$save();
            };
            $scope.saveTestFoodProduct = function () {
                $scope.created.$save();
            };
            $scope.findFoodProductById = function (foodProductId) {
                for (var i = 0; i < $scope.foodProducts.length; i++) {
                    if ($scope.foodProducts[i].id == foodProductId) {
                        return $scope.foodProducts[i];
                    }
                }
            };
      
            //**************************************************************************************************

            //*********************** NUTRITION UNIT ****************************//
            $scope.nutritionUnitResource = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/nutritionUnits/:id",
                    {email: userEmail, date: "@nutritionDay.date", id: "@id"});
            $scope.getNutritionUnits = function (nutritionDay) {
                $scope.nutritionUnitResource.query({date: nutritionDay.date}, function (data) {
                    var nutritionUnits = mapToResource(data, $scope.nutritionUnitResource);
                    for (var i = 0; i < nutritionUnits.length; i++) {

                        for (var j = 0; j < nutritionUnits[i].foodProducts.length; j++) {
                            var grams = nutritionUnits[i].foodProducts[j].grams;
                            var foodProduct = $scope.findFoodProductById(nutritionUnits[i].foodProducts[j].id);
                            nutritionUnits[i].foodProducts[j].macronutrients = $scope.calculateMacronutrients(foodProduct, grams);
                        }
                    }
                    nutritionDay.nutritionUnits = nutritionUnits;
                });
            };
            //*********************** NUTRITION DAY ****************************//
            $scope.nutritionDaysResource = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date", {email: userEmail, date: "@date"}, {
                query: {
                    isArray: false
                }
            });
            $scope.getNutritionDays = function () {
                $scope.nutritionDaysResource.query({email: userEmail}, function (data) {
                    $scope.nutritionDays = data;
                    $scope.nutritionDays.content = mapToResource(data.content, $scope.nutritionDaysResource);
                    for (var i = 0; i < $scope.nutritionDays.content.length; i++) {
                        $scope.getNutritionUnits($scope.nutritionDays.content[i]);
                    }
                });
            };
            $scope.createOrEditNutritionDay = function (nutritionDay) {
                $scope.currentNutritionDay = nutritionDay ? angular.copy(nutritionDay) : {};
                $scope.displayMode.editNutritionDay = true;
            };
            //**********************************************************************//

            // ********************** CALCULATION PERFORMANCE AND TESTS *********************//
            $scope.getFoodProducts();
            $scope.getNutritionDays();

            $scope.test = {};
            $scope.test.input = 20;
            $scope.test.macronutrients = $scope.test.input * 2;
            $scope.calculateMacronutrients = function (foodProduct, grams) {
                $scope.test.macronutrients = macronutrientsCalculator.calculateMacronutrients(foodProduct, grams);
            }


            $scope.changeProteins = function (foodProduct, grams) {
                foodProduct.proteins = grams;
            };


        });

