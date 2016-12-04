/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// OPCJE - ZMIENIĆ transformRequest ale to jest słabe to trzeba zmieniać wszystko ;p  | ZROBIĆ TAK BY KONKRETNE FOOD PRODUCTY BEZ LINKÓW BYŁY RÓWNIEŻ RESOURCAMI
angular.module("goToGoalModule")
        .controller("nutritionController", function ($scope, $rootScope,
                macronutrientsCalculator, nutritionResourceHandlerFactory, authenticationHandler, $location) {

      
            $scope.currentNutritionDay = {};
            var nutritionResourceHandler = null;
            $rootScope.displayMode.setView();
            $rootScope.alerter.setDefault();

          

            var findIndexInArrayById = function (id, array) {
                for (var i = 0; i < array.length; i++) {
                    if (id == array[i].id) {
                        return i;
                    }
                }
                return null;
            };



            $scope.getNutritionDaysPage = function (page, date) {
                var params = {};
                if (page && angular.isNumber(page)) {
                    params.page = page;
                }
                if (date){
                    params.date = date;
                }
                nutritionResourceHandler.getNutritionDaysPage(params).$promise.then(function (nutritionDaysPage) {
                    $scope.nutritionDays = nutritionDaysPage;
                    $scope.nutritionDays.content = nutritionResourceHandler.mapNutritionDaysToResource(nutritionDaysPage.content);
                    for (var i = 0; i < $scope.nutritionDays.content.length; i++) {
                        nutritionResourceHandler.getMealsSetItInNutritionDayCalculateMacronutrients($scope.nutritionDays.content[i]);
                    }
                });
            };


            authenticationHandler.getLoggedUserRequest().then(function (response) {
                nutritionResourceHandler = nutritionResourceHandlerFactory.instantiate(response.data);
                nutritionResourceHandler.getFoodProducts().$promise.then(function (foodProducts) {
                    $scope.foodProducts = foodProducts;
                });
                $scope.getNutritionDaysPage();
            });




            $scope.createOrEditNutritionDay = function (nutritionDay) {
                if (nutritionDay) {
                    $scope.currentNutritionDay.content = angular.copy(nutritionDay);
                } else {
                    $scope.currentNutritionDay.content = nutritionResourceHandler.getEmptyNutritionDay();
                }
                $rootScope.alerter.hide();
                $rootScope.displayMode.setCreator();
                
            };
            
            $scope.cancelNutritionDayCreation = function(currentNutritionDay){
                currentNutritionDay={};
                $rootScope.displayMode.setView();
            };

            $scope.addFoodProductToMealCalculateMacronutrients = function (foodProduct, meal, nutritionDay, grams) {
                if (angular.isObject(foodProduct) && angular.isObject(meal)) {
                    var eatenFoodProduct = angular.copy(foodProduct);
                    angular.isNumber(grams) ? eatenFoodProduct.grams = grams : eatenFoodProduct.grams = 100;
                    eatenFoodProduct.macronutrients = macronutrientsCalculator.calculateMacronutrients(foodProduct, eatenFoodProduct.grams);
                    meal.eatenFoodProducts.push(eatenFoodProduct);
                    meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
                    nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
                }
            };

            $scope.addEmptyMealToNutritionDay = function (nutritionDay) {
                console.log(nutritionResourceHandler.getEmptyMeal());
                nutritionDay.meals.push(nutritionResourceHandler.getEmptyMeal());
            };
            $scope.updateEatenFoodProductMealNutritionDayMacronutrients = function (eatenFoodProduct, meal, nutritionDay) {
                eatenFoodProduct.macronutrients = macronutrientsCalculator.updateMacronutrients(eatenFoodProduct, eatenFoodProduct.grams, eatenFoodProduct.macronutrients);
                meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
                nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
            };

            $scope.deleteEatedFoodProductFromMealUpdateMealMacronutrients = function (eatenFoodProduct, meal) {
                meal.eatenFoodProducts.splice(meal.eatenFoodProducts.indexOf(eatenFoodProduct), 1);
                meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
            };

            $scope.deleteMealFromNutritionDayUpdateMacronutrients = function (nutritionDay, meal) {
                nutritionDay.meals.splice(nutritionDay.meals.indexOf(meal), 1);
                nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
            };


            $scope.saveCurrentNutritionDay = function (currentNutritionDay) {
                if (angular.isDefined(currentNutritionDay.content.id)) {
                    $scope.updateNutritionDay(currentNutritionDay.content);
                } else {
                    $scope.createNutritionDay(currentNutritionDay.content);
                }
            };

            $scope.deleteNutritionDay = function (nutritionDay) {
                nutritionResourceHandler.deleteNutritionDay(nutritionDay).$promise.then(function () {
                    var index = findIndexInArrayById(nutritionDay.id, $scope.nutritionDays.content);
                    if (angular.isNumber(index)) {
                        $scope.nutritionDays.content.splice(index, 1);
                    }
                    $rootScope.alerter.setSuccessMessage("Dziennik żywieniowy został usunięty");
                    $rootScope.displayMode.setView();
                    $location.path("/nutritionDays");
                });
            };

            $scope.updateNutritionDay = function (nutritionDay) {
                nutritionResourceHandler.updateNutritionDay(nutritionDay).$promise.then(function (nutritionDayResponse) {
                    var index = findIndexInArrayById(nutritionDayResponse.id, $scope.nutritionDays.content);
                    console.log(index);
                    if (angular.isNumber(index)) {
                        macronutrientsCalculator.calculateMacronutrientsInNutritionDay(nutritionDayResponse);
                        $scope.nutritionDays.content[index] = nutritionDayResponse;
                    }
                    $rootScope.alerter.setSuccessMessage("Dziennik żywieniowy został zaktualizowany");
                    $rootScope.displayMode.setView();
                    $location.path("/nutritionDays");
                }, function (error) {
                    $rootScope.alerter.setWarnintMessage(error.data.message);
                    $location.path("/nutritionDays");
                });

            };

            $scope.createNutritionDay = function (nutritionDay) {
                nutritionResourceHandler.createNutritionDay(nutritionDay).$promise.then(function (nutritionDayResponse) {
                    macronutrientsCalculator.calculateMacronutrientsInNutritionDay(nutritionDayResponse);
                    $scope.nutritionDays.content.push(nutritionDayResponse);
                    $rootScope.alerter.setSuccessMessage("Dziennik żywieniowy został pomyślnie zapisany");
                    $rootScope.displayMode.setView();
                    $location.path("/nutritionDays");
                }, function (error) {
                    $rootScope.alerter.setWarnintMessage(error.data.message);
                    $location.path("/nutritionDays");
                });
            };
        });

