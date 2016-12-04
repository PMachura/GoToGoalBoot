/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("resourceHandlerModule")
        .factory("nutritionResourceHandlerFactory", function ($resource, macronutrientsCalculator) {
            var resourceUrlPrefix = "http://localhost:8080/api";
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

            var instantiate = function (user) {
                var nutritionResourceHandler = new Object();
                nutritionResourceHandler.user = angular.copy(user);
                nutritionResourceHandler.mapToResource = mapToResource;
                nutritionResourceHandler.foodProductsResource = $resource(resourceUrlPrefix + "/foodProducts/:id", {id: "@id"});
                nutritionResourceHandler.mealResource = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:nutritionDayId/meals/:id",
                        {userId: user.id, nutritionDayId: "@nutritionDay.id", id: "@id"},
                        {
                            create: {
                                method: "POST"
                            },
                            save: {
                                method: "PUT"
                            }
                        });
                nutritionResourceHandler.nutritionDaysResource = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:id", {userId: user.id, id: "@id"}, {
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
                nutritionResourceHandler.mapNutritionDaysToResource = function(nutritionDays){
                    return this.mapToResource(nutritionDays, this.nutritionDaysResource);
                };
                nutritionResourceHandler.getEmptyNutritionDay = function() {
                    return {
                        date: "2016-11-11",
                        meals: []
                    };
                };
                nutritionResourceHandler.getEmptyMeal = function () {
                    return {
                        eatenFoodProducts: [],
                        time: "12:00:00"
                    };
                },
                
                nutritionResourceHandler.getFoodProducts = function () {
                    return this.foodProductsResource.query();
                },
                nutritionResourceHandler.getMealsForNutritionDay = function (nutritionDay) {
                    params = {nutritionDayId: nutritionDay.id};
                    return this.mealResource.query(params);
                },
                nutritionResourceHandler.getMealsSetItInNutritionDayCalculateMacronutrients = function (nutritionDay) {
                    this.getMealsForNutritionDay(nutritionDay).$promise.then(function (meals) {
                        macronutrientsCalculator.calculateMacronutrientsInEatenFoodProductsInMeals(meals);
                        macronutrientsCalculator.sumMealMacronutrientsInMeals(meals);
                        nutritionDay.meals = meals;
                        nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
                    });
                },
                nutritionResourceHandler.getNutritionDaysPage = function (params) {
                    return this.nutritionDaysResource.query(params);
                },
                nutritionResourceHandler.createNutritionDay = function (nutritionDay) {
                    return this.nutritionDaysResource.create({date: ""}, nutritionDay);
                },
                nutritionResourceHandler.updateNutritionDay = function (nutritionDay) {
                    return this.nutritionDaysResource.save(nutritionDay);
                },
                nutritionResourceHandler.deleteNutritionDay =  function (nutritionDay) {
                    return this.nutritionDaysResource.delete(nutritionDay);
                };
                return nutritionResourceHandler;
            };
           
            return {
               instantiate: instantiate
            };

        });

