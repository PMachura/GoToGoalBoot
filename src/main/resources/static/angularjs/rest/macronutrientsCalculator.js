/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("macronutrientsCalculatorModule", [])
        .factory("macronutrientsCalculator", function () {
            var generateMacronutrientsObject = function () {
                return macronutrients = {
                    proteins: {
                        grams: 0,
                        calories: {
                            amount: 0,
                            percentage: 0
                        }
                    },
                    carbohydrates: {
                        grams: 0,
                        calories: {
                            amount: 0,
                            percentage: 0
                        }
                    },
                    fats: {
                        grams: 0,
                        calories: {
                            amount: 0,
                            percentage: 0
                        }
                    },
                    allCalories: 0
                };
            };

            var calculateMacronutrientsCaloriesAmount = function (macronutrientsObject, foodProduct, grams) {
                var multiplier = grams / 100;
                macronutrientsObject.proteins.calories.amount = multiplier * foodProduct.proteins * 4;
                macronutrientsObject.carbohydrates.calories.amount = multiplier * foodProduct.carbohydrates * 4;
                macronutrientsObject.fats.calories.amount = multiplier * foodProduct.fats * 9;
                macronutrientsObject.allCalories = multiplier * foodProduct.calories;
                return macronutrientsObject;
            };
            var calculateMacronutrientsCaloriesPercentage = function (macronutrientsObject) {
                var multiplier = 100 / macronutrientsObject.allCalories;
                macronutrientsObject.proteins.calories.percentage = macronutrientsObject.proteins.calories.amount * multiplier;
                macronutrientsObject.carbohydrates.calories.percentage = macronutrientsObject.carbohydrates.calories.amount * multiplier;
                macronutrientsObject.fats.calories.percentage = macronutrientsObject.fats.calories.amount * multiplier;
                return macronutrientsObject;

            };
            var calculateMacronutrientsCalories = function (foodProduct, grams) {
                var macronutrientsObject = generateMacronutrientsObject();
                calculateMacronutrientsCaloriesAmount(macronutrientsObject, foodProduct, grams);
                calculateMacronutrientsCaloriesPercentage(macronutrientsObject);
                return macronutrientsObject;
            };
            var calculateMacronutrientsGrams = function (foodProduct, grams) {
                var macronutrientsObject = generateMacronutrientsObject();
                var multiplier = grams / 100;
                macronutrientsObject.proteins.grams = multiplier * foodProduct.proteins;
                macronutrientsObject.carbohydrates.grams = multiplier * foodProduct.carbohydrates;
                macronutrientsObject.fats.grams = multiplier * foodProduct.fats;
                return macronutrientsObject;
            };

            return {
                /**
                 * Function which calcuate macronutrients for eatenFoodProduct for the first time
                 * @param {type} foodProduct
                 * @param {type} grams
                 * @returns {macronutrients}
                 */
                calculateMacronutrients: function (foodProduct, grams) {
                    var macronutrients = generateMacronutrientsObject();
                    if (angular.isObject(foodProduct) && angular.isNumber(grams)) {
                        var calculatedGrams = calculateMacronutrientsGrams(foodProduct, grams);
                        var calculatedCalories = calculateMacronutrientsCalories(foodProduct, grams);

                        macronutrients.proteins.grams = calculatedGrams.proteins.grams;
                        macronutrients.proteins.calories.amount = calculatedCalories.proteins.calories.amount;
                        macronutrients.proteins.calories.percentage = calculatedCalories.proteins.calories.percentage;

                        macronutrients.carbohydrates.grams = calculatedGrams.carbohydrates.grams;
                        macronutrients.carbohydrates.calories.amount = calculatedCalories.carbohydrates.calories.amount;
                        macronutrients.carbohydrates.calories.percentage = calculatedCalories.carbohydrates.calories.percentage;

                        macronutrients.fats.grams = calculatedGrams.fats.grams;
                        macronutrients.fats.calories.amount = calculatedCalories.fats.calories.amount;
                        macronutrients.fats.calories.percentage = calculatedCalories.fats.calories.percentage;

                        macronutrients.allCalories = calculatedCalories.allCalories;

                    }
                    return macronutrients;

                },
                /**
                 * Function which calculate the macronutrients for the eatenFoodProduct for the second time,
                 * while the percentage calculation have been done, and are not necessery to repeat 
                 * @param {type} foodProduct
                 * @param {type} grams
                 * @param {type} macronutrients
                 * @returns {unresolved}
                 */
                updateMacronutrients: function (foodProduct, grams, macronutrients) {
                    if (angular.isObject(foodProduct) && angular.isNumber(grams) && angular.isObject(macronutrients)) {
                        var calculatedGrams = calculateMacronutrientsGrams(foodProduct, grams);
                        var calculatedCalories = calculateMacronutrientsCaloriesAmount(generateMacronutrientsObject(), foodProduct, grams);

                        macronutrients.proteins.grams = calculatedGrams.proteins.grams;
                        macronutrients.proteins.calories.amount = calculatedCalories.proteins.calories.amount;
                        
                        macronutrients.carbohydrates.grams = calculatedGrams.carbohydrates.grams;
                        macronutrients.carbohydrates.calories.amount = calculatedCalories.carbohydrates.calories.amount;
         
                        macronutrients.fats.grams = calculatedGrams.fats.grams;
                        macronutrients.fats.calories.amount = calculatedCalories.fats.calories.amount;

                        macronutrients.allCalories = calculatedCalories.allCalories;
                    }
                    return macronutrients;

                },
                /**
                 * Sum macronutrients for nutritionUnit basing on the eatenFoodProducts
                 * @param {type} nutritionUnit
                 * @returns {macronutrients}
                 */
                sumMacronutrientsForNutritionUnit: function (nutritionUnit) {
                    if (angular.isObject(nutritionUnit)) {
                        var macronutrients = generateMacronutrientsObject();
                        for (var i = 0; i < nutritionUnit.eatenFoodProducts.length; i++) {
                            macronutrients.proteins.grams += nutritionUnit.eatenFoodProducts[i].macronutrients.proteins.grams;
                            macronutrients.proteins.calories.amount += nutritionUnit.eatenFoodProducts[i].macronutrients.proteins.calories.amount;

                            macronutrients.carbohydrates.grams += nutritionUnit.eatenFoodProducts[i].macronutrients.carbohydrates.grams;
                            macronutrients.carbohydrates.calories.amount += nutritionUnit.eatenFoodProducts[i].macronutrients.carbohydrates.calories.amount;

                            macronutrients.fats.grams += nutritionUnit.eatenFoodProducts[i].macronutrients.fats.grams;
                            macronutrients.fats.calories.amount += nutritionUnit.eatenFoodProducts[i].macronutrients.fats.calories.amount;

                            macronutrients.allCalories += nutritionUnit.eatenFoodProducts[i].macronutrients.allCalories;
                            calculateMacronutrientsCaloriesPercentage(macronutrients);
                        }
                        return macronutrients;
                    } else {
                        return null;
                    }
                }

            };
        });
