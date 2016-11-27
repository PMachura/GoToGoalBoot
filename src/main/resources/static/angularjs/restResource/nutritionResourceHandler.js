/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("resourceHandlerModule")
        .factory("nutritionResourceHandler", function ($resource, macronutrientsCalculator) {
            var resourceUrlPrefix = "http://localhost:8080/api";
            var user = null;
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
//            var eatenFoodProducts = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:nutritionDayId/meals/:mealId/mealsFoodProducts/:id",
//                    {email: userEmail, id: "@relationEntityId"});

            var mealResource = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:nutritionDayId/meals/:id",
                    { nutritionDayId: "@nutritionDay.id", id: "@id"},
                    {
                        create: {
                            method: "POST"
                        },
                        save: {
                            method: "PUT"
                        }
                    });
                    

            var nutritionDaysResource = $resource(resourceUrlPrefix + "/users/:userId/nutritionDays/:id", {id: "@id"}, {
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
                setUser :function(givenUser){
                  user = givenUser;  
                },
                
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
                    params = {nutritionDayId: nutritionDay.id, userId: user.id};
                    return mealResource.query(params);
                },
                getMealsSetItInNutritionDayCalculateMacronutrients: function (nutritionDay) {
                    this.getMealsForNutritionDay(nutritionDay).$promise.then(function (meals) {
                        macronutrientsCalculator.calculateMacronutrientsInEatenFoodProductsInMeals(meals);
                        macronutrientsCalculator.sumMealMacronutrientsInMeals(meals);
                        nutritionDay.meals = meals;
                        nutritionDay.macronutrients = macronutrientsCalculator.sumNutritionDayMacronutrients(nutritionDay);
                    });
                },
                getNutritionDaysPage: function (params) {
                    params.userId = user.id;
                    return nutritionDaysResource.query(params);
                },
                
                createNutritionDay: function(nutritionDay){
                    return nutritionDaysResource.create({date: "", userId:user.id}, nutritionDay);
                },
                updateNutritionDay: function(nutritionDay){
                    return nutritionDaysResource.save({userId: user.id}, nutritionDay);
                },
                deleteNutritionDay: function(nutritionDay){
                    return nutritionDaysResource.delete({userId: user.id},nutritionDay);
                }
                

            };

        });

