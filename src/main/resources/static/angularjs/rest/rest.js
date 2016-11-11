/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// OPCJE - ZMIENIĆ transformRequest ale to jest słabe to trzeba zmieniać wszystko ;p  | ZROBIĆ TAK BY KONKRETNE FOOD PRODUCTY BEZ LINKÓW BYŁY RÓWNIEŻ RESOURCAMI
angular.module("restModule", ["ngResource", "macronutrientsCalculatorModule", "ui.select", "ngSanitize"])
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

            //*********************** NUTRITIONUNITFOODPRODUCTS *********************************************//
            $scope.eatenFoodProducts = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/nutritionUnits/:nutritionUnitId/nutritionUnitsFoodProducts/:id",
                    {email: userEmail, id: "@id"});

            $scope.deleteEatenFoodProduct = function (eatenFoodProduct, params) {
                $scope.eatenFoodProducts.delete(params, eatenFoodProduct)
            }

            //*********************** NUTRITION UNIT ****************************//
            $scope.nutritionUnitResource = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/nutritionUnits/:id",
                    {email: userEmail, date: "@nutritionDay.date", id: "@id"});
            $scope.getNutritionUnits = function (nutritionDay) {
                $scope.nutritionUnitResource.query({date: nutritionDay.date}, function (data) {
                    var nutritionUnits = mapToResource(data, $scope.nutritionUnitResource);
                    for (var i = 0; i < nutritionUnits.length; i++) {
                        for (var j = 0; j < nutritionUnits[i].eatenFoodProducts.length; j++) {
                            var grams = nutritionUnits[i].eatenFoodProducts[j].grams;
                            var foodProduct = $scope.findFoodProductById(nutritionUnits[i].eatenFoodProducts[j].id);
                            nutritionUnits[i].eatenFoodProducts[j].macronutrients = macronutrientsCalculator.calculateMacronutrients(foodProduct, grams);
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
            $scope.test = {};
            $scope.test.returnedValue = 5;

            $scope.editedNutritionUnit = {};

            $scope.getFoodProducts();
            $scope.getNutritionDays();


            $scope.addFoodProductToNutritionUnit = function (foodProduct, nutritionUnit, grams) {
                if (angular.isObject(foodProduct) && angular.isObject(nutritionUnit)) {
                    var eatenFoodProduct = {};
                    eatenFoodProduct.id = foodProduct.id;
                    eatenFoodProduct.name = foodProduct.name;
                    eatenFoodProduct.category = foodProduct.category;
                    angular.isNumber(grams) ? eatenFoodProduct.grams = grams : eatenFoodProduct.grams = 100;                   
                    eatenFoodProduct.macronutrients = macronutrientsCalculator.calculateMacronutrients(foodProduct, eatenFoodProduct.grams);
                    nutritionUnit.eatenFoodProducts.push(eatenFoodProduct);
                    nutritionUnit.macronutrients = macronutrientsCalculator.sumMacronutrientsForNutritionUnit(nutritionUnit);
                }
            };

            $scope.getNutritionUnitToEdit = function () {

                $scope.editedNutritionUnit.eatenFoodProductsToDelete = [];
                $scope.editedNutritionUnit.content = angular.copy($scope.nutritionDays.content[0].nutritionUnits[0]);
                $scope.editedNutritionUnit.content.macronutrients = macronutrientsCalculator.sumMacronutrientsForNutritionUnit($scope.editedNutritionUnit.content);
            };
            $scope.updateEatenFoodProduct = function (eatenFoodProduct) {
                eatenFoodProduct.macronutrients = macronutrientsCalculator.updateMacronutrients($scope.findFoodProductById(eatenFoodProduct.id), eatenFoodProduct.grams, eatenFoodProduct.macronutrients);
                $scope.editedNutritionUnit.content.macronutrients = macronutrientsCalculator.sumMacronutrientsForNutritionUnit($scope.editedNutritionUnit.content);
            };

            $scope.deleteEatedFoodProductFromEditedNutritionUnit = function (eatenFoodProduct) {
                $scope.editedNutritionUnit.eatenFoodProductsToDelete.push(eatenFoodProduct);
                $scope.editedNutritionUnit.content.eatenFoodProducts.splice($scope.editedNutritionUnit.content.eatenFoodProducts.indexOf(eatenFoodProduct), 1);
                $scope.editedNutritionUnit.content.macronutrients = macronutrientsCalculator.sumMacronutrientsForNutritionUnit($scope.editedNutritionUnit.content);

            };
            
            $scope.saveEditedNutritionUnit = function(nutritionUnit){
                delete nutritionUnit.nutritionDay.user;
                nutritionUnit.$save();
            };




        });

