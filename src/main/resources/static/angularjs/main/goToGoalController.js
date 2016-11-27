/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("goToGoalModule")
        .controller("goToGoalController", function($scope, $rootScope){
            $rootScope.messageHandler = {
                text: "",
                display: false,
                set: function(message){
                    this.text = message;
                    this.display = true;
                },
                hide: function(){
                   this.display = false; 
                }
            };
});