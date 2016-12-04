/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// można by zmienić nazwę na serviceCommunicationMoudle
angular.module("resourceHandlerModule", ["ngResource", "macronutrientsCalculatorModule"])
        .config(function ($routeProvider, $httpProvider) {
            $routeProvider.when("/nutritionDays", {
                templateUrl: "/templates/nutrition/index.html"
            });
            $routeProvider.when("/register", {
                templateUrl: "/templates/user/register.html"
            });
            $routeProvider.when("/workoutDays", {
                templateUrl: "/templates/workout/index.html"
            });
             
            
            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
        });
       
       
       