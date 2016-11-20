/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// OPCJE - ZMIENIĆ transformRequest ale to jest słabe to trzeba zmieniać wszystko ;p  | ZROBIĆ TAK BY KONKRETNE FOOD PRODUCTY BEZ LINKÓW BYŁY RÓWNIEŻ RESOURCAMI
angular.module("restModule", ["ngResource", "macronutrientsCalculatorModule", "nutritionResourceModule", "ui.select", "ngSanitize"])
        .constant("userEmail", "p@p")
        .constant("userId", "1")
        .constant("resourceUrlPrefix", "http://localhost:8080/api")
        .controller("restController", function ($scope, userEmail,
                macronutrientsCalculator, nutritionResourceHandler) {

            $scope.test = {};
            $scope.currentNutritionDay = {};

            nutritionResourceHandler.getFoodProducts().$promise.then(function (foodProducts) {
                $scope.foodProducts = foodProducts;
            });

            nutritionResourceHandler.getNutritionDaysPage().$promise.then(function (nutritionDaysPage) {
                $scope.nutritionDays = nutritionDaysPage;
                $scope.nutritionDays.content = nutritionResourceHandler.mapNutritionDaysToResource(nutritionDaysPage.content);
                for (var i = 0; i < $scope.nutritionDays.content.length; i++) {
                    nutritionResourceHandler.getMealsSetItInNutritionDayCalculateMacronutrients($scope.nutritionDays.content[i]);
                }
            });
            
            $scope.createOrEditNutritionDay = function (nutritionDay) {
                if (nutritionDay) {
                    $scope.currentNutritionDay.content = angular.copy(nutritionDay);
                } else {
                    $scope.currentNutritionDay.content = nutritionResourceHandler.getEmptyNutritionDay();
                }
            };


            

            $scope.currentMeal = {};

            $scope.addFoodProductToMeal = function (foodProduct, meal, grams) {
                if (angular.isObject(foodProduct) && angular.isObject(meal)) {
                    var eatenFoodProduct = angular.copy(foodProduct);
                    angular.isNumber(grams) ? eatenFoodProduct.grams = grams : eatenFoodProduct.grams = 100;
//                    eatenFoodProduct.id = foodProduct.id;
//                    eatenFoodProduct.name = foodProduct.name;
//                    eatenFoodProduct.category = foodProduct.category;                
//                    eatenFoodProduct.carbohydrates = foodProduct.carbohydrates;
//                    eatenFoodProduct.fats = foodProduct.fats;
//                    eatenFoodProduct.proteins = foodProduct.proteins;
//                    eatenFoodProduct.calories = foodProduct.calories;
                    eatenFoodProduct.macronutrients = macronutrientsCalculator.calculateMacronutrients(foodProduct, eatenFoodProduct.grams);
                    meal.eatenFoodProducts.push(eatenFoodProduct);
                    meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
                }
            };

            $scope.addEmptyMealToNutritionDay = function (nutritionDay) {
                nutritionDay.meals.push(nutritionResourceHandler.getEmptyMeal());
            };
            $scope.updateEatenFoodProductAndMealMacronutrients = function (eatenFoodProduct, meal) {
                eatenFoodProduct.macronutrients = macronutrientsCalculator.updateMacronutrients(eatenFoodProduct, eatenFoodProduct.grams, eatenFoodProduct.macronutrients);
                meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
            };

            $scope.deleteEatedFoodProductFromMealUpdateMealMacronutrients = function (eatenFoodProduct,meal) {
                meal.eatenFoodProducts.splice(meal.eatenFoodProducts.indexOf(eatenFoodProduct), 1);
                meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
            };


//funkcje oparte o serwer

            $scope.createNutritionDay = function (nutritionDay) {
                new $scope.nutritionDaysResource(nutritionDay).$save({date: ""}, function (nutritionDay) {
                    $scope.test.nutritionDay = nutritionDay;
                });
            };
            $scope.updateMeal = function (meal) {
                meal.$save(function (meal) {
                    $scope.test.meal = meal;

                    macronutrientsCalculator.calculateMacronutrientsInEatenFoodProducts(meal.eatenFoodProducts);
                    meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
                    for (var i = 0; i < $scope.nutritionDays.meals.length; i++) {
                        if ($scope.nutritionDays.content.meals[i].id == id) {
                            $scope.nutritionDays.content.meals[i] = meal;
                            break;
                        }
                    }
                });
            };

            $scope.createMeal = function (meal) {
                new $scope.mealResource(meal).$create(function (meal) {
                    $scope.test.createdMeal = meal;
                });
            };

            $scope.saveCurrentMeal = function (currentMeal) {
                if (angular.isDefined(currentMeal.content.id)) {
                    $scope.updateMeal(currentMeal.content);
                } else {
                    $scope.createMeal(currentMeal.content);
                }

            };


            $scope.deleteMeal = function (meal, nutritionDay) {
                var findMealIndexFromNutritionDay = function (meal, nutritionDay) {
                    for (var i = 0; i < nutritionDay.meals.length; i++) {
                        if (nutritionDay.meals[i].id == meal.id) {
                            return i;
                        }
                    }
                    return null;
                };
                meal.$delete().then(function () {
                    var index = findMealIndexFromNutritionDay(meal, nutritionDay);
                    nutritionDay.splice(index, 1);
                });
            };



        });

