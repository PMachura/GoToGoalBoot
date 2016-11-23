/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("nutritionResourceModule", ["ngResource", "macronutrientsCalculatorModule"])
        .factory("nutritionResourceHandler", function ($resource, macronutrientsCalculator) {
            var resourceUrlPrefix = "http://localhost:8080/api";
            var userEmail = "p@p";
            var userId = 1;
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


            var foodProductsResource = $resource(resourceUrlPrefix + "/foodProducts/:id", {id: "@id"});
            var eatenFoodProducts = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:nutritionDayId/meals/:mealId/mealsFoodProducts/:id",
                    {email: userEmail, id: "@relationEntityId"});

            var mealResource = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:nutritionDayId/meals/:id",
                    {userId: userId, nutritionDayId: "@nutritionDay.id", id: "@id"},
                    {
                        create: {
                            method: "POST"
                        },
                        save: {
                            method: "PUT"
                        }
                    });

            var nutritionDaysResource = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:id", {userId: userId, id: "@id"}, {
                query: {
                    isArray: false
                },
                create: {
                    method: "POST"
                },
                save: {
                    method: "PUT"
                }
            });

            return {
                mapNutritionDaysToResource: function (nutritionDays) {
                    return mapToResource(nutritionDays, nutritionDaysResource);
                },
                getEmptyNutritionDay: function () {
                    return {
                        date: "2016-11-11",
                        meals: []
                    };
                },
                getEmptyMeal: function () {
                    return {
                        eatenFoodProducts: [],
                        time: "12:00:00"
                    };
                },
                getFoodProducts: function () {
                    return foodProductsResource.query();
                },
                getMealsForNutritionDay: function (nutritionDay) {
                    return mealResource.query({nutritionDayId: nutritionDay.id});
                },
                getMealsSetItInNutritionDayCalculateMacronutrients: function (nutritionDay) {
                    this.getMealsForNutritionDay(nutritionDay).$promise.then(function (meals) {
                        macronutrientsCalculator.calculateMacronutrientsInEatenFoodProductsInMeals(meals);
                        macronutrientsCalculator.sumMealMacronutrientsInMeals(meals);
                        nutritionDay.meals = meals;
                        nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
                    });
                },
                getNutritionDaysPage: function () {
                    return nutritionDaysResource.query();
                },
                
                createNutritionDay: function(nutritionDay){
                    return nutritionDaysResource.create({date: ""}, nutritionDay);
                },
                updateNutritionDay: function(nutritionDay){
                    return nutritionDaysResource.save(nutritionDay);
                    console.log("Jestem w save");
                },
                deleteNutritionDay: function(nutritionDay){
                    return nutritionDaysResource.delete({},nutritionDay);
                }
                

            };

        });

