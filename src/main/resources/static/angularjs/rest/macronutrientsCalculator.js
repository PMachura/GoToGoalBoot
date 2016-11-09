/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("macronutrientsCalculatorModule", [])
        .factory("macronutrientsCalculator", function () {
            var generateCaloriesObject = function () {
                return {
                    proteins: {
                        amount: null,
                        percentage: null
                    }, carbohydrates: {
                        amount: null,
                        percentage: null
                    }, fats: {
                        amount: null,
                        percentage: null
                    },
                    allAmount: null
                };
            };

            var generateGramsObject = function () {
                return {
                    proteins: null,
                    carbohydrates: null,
                    fats: null
                };
            };

            var generateMacronutrientsObject = function () {
                return macronutrients = {
                    proteins: {
                        grams: null,
                        calories: {
                            amount: null,
                            percentage: null
                        }
                    },
                    carbohydrates: {
                        grams: null,
                        calories: {
                            amount: null,
                            percentage: null
                        }
                    },
                    fats: {
                        grams: null,
                        calories: {
                            amount: null,
                            percentage: null
                        }
                    },
                    allCalories: null
                };
            };

            var calculateMacronutrientsCaloriesAmount = function (caloriesObject, foodProduct, grams) {
                var multiplier = grams / 100;
                caloriesObject.proteins.amount = multiplier * foodProduct.proteins * 4;
                caloriesObject.carbohydrates.amount = multiplier * foodProduct.carbohydrates * 4;
                caloriesObject.fats.amount = multiplier * foodProduct.fats * 9;
                caloriesObject.allAmount = multiplier * foodProduct.calories;
                return caloriesObject;
            };
            var calculateMacronutrientsCaloriesPercentage = function (caloriesObject) {
                var multiplier = 100 / caloriesObject.allAmount;
                caloriesObject.proteins.percentage = caloriesObject.proteins.amount * multiplier;
                caloriesObject.carbohydrates.percentage = caloriesObject.carbohydrates.amount * multiplier;
                caloriesObject.fats.percentage = caloriesObject.fats.amount * multiplier;
                return caloriesObject;

            };
            var calculateMacronutrientsCalories = function (foodProduct, grams) {
                var caloriesObject = generateCaloriesObject();
                calculateMacronutrientsCaloriesAmount(caloriesObject, foodProduct, grams);
                calculateMacronutrientsCaloriesPercentage(caloriesObject);
                return caloriesObject;
            };
            var calculateMacronutrientsGrams = function (foodProduct, grams) {
                var calculatedGrams = generateGramsObject();
                var multiplier = grams / 100;
                calculatedGrams.proteins = multiplier * foodProduct.proteins;
                calculatedGrams.carbohydrates = multiplier * foodProduct.carbohydrates;
                calculatedGrams.fats = multiplier * foodProduct.fats;
                return calculatedGrams;
            };

            return {
                calculateMacronutrients: function (foodProduct, grams) {
                    var calculatedGrams = calculateMacronutrientsGrams(foodProduct, grams);
                    var calculatedCalories = calculateMacronutrientsCalories(foodProduct, grams);
                    var macronutrients = generateMacronutrientsObject();

                    macronutrients.proteins.grams = calculatedGrams.proteins;
                    macronutrients.proteins.calories.amount = calculatedCalories.proteins.amount;
                    macronutrients.proteins.calories.percentage = calculatedCalories.proteins.percentage;

                    macronutrients.carbohydrates.grams = calculatedGrams.carbohydrates;
                    macronutrients.carbohydrates.calories.amount = calculatedCalories.carbohydrates.amount;
                    macronutrients.carbohydrates.calories.percentage = calculatedCalories.carbohydrates.percentage;

                    macronutrients.fats.grams = calculatedGrams.fats;
                    macronutrients.fats.calories.amount = calculatedCalories.fats.amount;
                    macronutrients.fats.calories.percentage = calculatedCalories.fats.percentage;

                    macronutrients.allCalories = calculatedCalories.allAmount;
                    
                    return macronutrients;
                }
            };
        });
