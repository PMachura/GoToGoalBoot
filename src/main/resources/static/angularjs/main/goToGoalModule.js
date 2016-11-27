/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// OPCJE - ZMIENIĆ transformRequest ale to jest słabe to trzeba zmieniać wszystko ;p  | ZROBIĆ TAK BY KONKRETNE FOOD PRODUCTY BEZ LINKÓW BYŁY RÓWNIEŻ RESOURCAMI
angular.module("goToGoalModule", ["ngResource", "macronutrientsCalculatorModule", "ngRoute", "resourceHandlerModule", "ui.select", "ngSanitize"])
        .config(function ($routeProvider, $httpProvider) {
            $routeProvider.when("/nutritionDays", {
                templateUrl: "/templates/nutrition/index.html"
            });
            $routeProvider.when("/register", {
                templateUrl: "/templates/user/register.html"
            });
             
            
            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
        });

