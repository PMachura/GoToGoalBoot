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

            $scope.test = {};

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


            //**************************************************************************************************

            //*********************** MEALS FOODPRODUCTS *********************************************//
            $scope.eatenFoodProducts = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/meals/:mealId/mealsFoodProducts/:id",
                    {email: userEmail, id: "@relationEntityId"});



            //*********************** MEAL ****************************//
            $scope.mealResource = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/meals/:id",
                    {email: userEmail, date: "@nutritionDay.date", id: "@id"},
                    {
                        create: {
                            method: "POST"
                        },
                        save: {
                            method: "PUT"
                        }
                    });

            $scope.getMealsAndSetItInNutritionDay = function (nutritionDay) {
                $scope.mealResource.query({date: nutritionDay.date}, function (meals) {
                    macronutrientsCalculator.calculateMacronutrientsInEatenFoodProductsInMeals(meals);
                    macronutrientsCalculator.sumMealMacronutrientsInMeals(meals);
                    nutritionDay.meals = meals;
                    nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
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
                        $scope.getMealsAndSetItInNutritionDay($scope.nutritionDays.content[i]);

                    }
                });
            };


            $scope.createOrEditNutritionDay = function (nutritionDay) {
                $scope.currentNutritionDay = {};
                $scope.test.nutritionDay = $scope.currentNutritionDay;
                if (nutritionDay) {
                    $scope.currentNutritionDay.content = angular.copy(nutritionDay);
                } else {
                    $scope.currentNutritionDay.content = {
                        date: "2016-11-11",
                        meals: []
                    };
                }
            };

            $scope.createNutritionDay = function (nutritionDay) {
                new $scope.nutritionDaysResource(nutritionDay).$save({date: ""}, function (nutritionDay) {
                    $scope.test.nutritionDay = nutritionDay;
                });
            };


            //**********************************************************************//

            // ********************** CALCULATION PERFORMANCE AND TESTS *********************//



            $scope.currentMeal = {};

            $scope.getFoodProducts();
            $scope.getNutritionDays();


            $scope.addFoodProductToMeal = function (foodProduct, meal, grams) {
                if (angular.isObject(foodProduct) && angular.isObject(meal)) {
                    var eatenFoodProduct = {};
                    eatenFoodProduct.id = foodProduct.id;
                    eatenFoodProduct.name = foodProduct.name;
                    eatenFoodProduct.category = foodProduct.category;
                    angular.isNumber(grams) ? eatenFoodProduct.grams = grams : eatenFoodProduct.grams = 100;
                    eatenFoodProduct.carbohydrates = foodProduct.carbohydrates;
                    eatenFoodProduct.fats = foodProduct.fats;
                    eatenFoodProduct.proteins = foodProduct.proteins;
                    eatenFoodProduct.calories = foodProduct.calories;
                    eatenFoodProduct.macronutrients = macronutrientsCalculator.calculateMacronutrients(foodProduct, eatenFoodProduct.grams);
                    meal.eatenFoodProducts.push(eatenFoodProduct);
                    meal.macronutrients = macronutrientsCalculator.sumMealMacronutrients(meal);
                }
            };

            $scope.createOrEditMeal = function (meal) {
                $scope.currentMeal = {};
                if (meal) {
                    $scope.currentMeal.content = angular.copy(meal);
                } else {
                    $scope.currentMeal.content =
                            {
                                nutritionDay: {
                                    id: $scope.currentNutritionDay.content.id,
                                    date: $scope.currentNutritionDay.content.date,
                                    user: $scope.currentNutritionDay.content.user

                                },
                                eatenFoodProducts: [],
                                time: "12:00:00"
                            };
                }
                $scope.currentNutritionDay.content.meals.push($scope.currentMeal.content);
                console.log($scope.currentMeal.content);
            };
            $scope.updateEatenFoodProductMacronutrients = function (eatenFoodProduct) {
                eatenFoodProduct.macronutrients = macronutrientsCalculator.updateMacronutrients(eatenFoodProduct, eatenFoodProduct.grams, eatenFoodProduct.macronutrients);
                $scope.currentMeal.content.macronutrients = macronutrientsCalculator.sumMealMacronutrients($scope.currentMeal.content);
            };

            $scope.deleteEatedFoodProductFromCurrentMeal = function (currentMeal, eatenFoodProduct) {
                currentMeal.content.eatenFoodProducts.splice(currentMeal.content.eatenFoodProducts.indexOf(eatenFoodProduct), 1);
                currentMeal.content.macronutrients = macronutrientsCalculator.sumMealMacronutrients(currentMeal.content);
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
                    console.log("CRETION SUCCESS");
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

