/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("nutritionResourceModule", ["ngResource"])
        .factory("nutritionResourceHandler", function ($resource) {
            var resourceUrlPrefix = "http://localhost:8080/api";
            var userEmail = "p@p";
            var userId = "1";
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
            var eatenFoodProducts = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/meals/:mealId/mealsFoodProducts/:id",
                    {email: userEmail, id: "@relationEntityId"});

            var mealResource = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date/meals/:id",
                    {email: userEmail, date: "@nutritionDay.date", id: "@id"},
                    {
                        create: {
                            method: "POST"
                        },
                        save: {
                            method: "PUT"
                        }
                    });

            var nutritionDaysResource = $resource(resourceUrlPrefix + "/users/:email/nutritionDays/:date", {email: userEmail, date: "@date"}, {
                query: {
                    isArray: false
                }
            });

            return {
                getFoodProducts: function () {
                    return foodProductsResource.query();
                },
                getMealsForNutritionDay: function (nutritionDay) {
                    return mealResource.query({date: nutritionDay.date});
                },
            };

        });

