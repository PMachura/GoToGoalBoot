var model = {
  user: "Przemek"
};

var foodProductApp = angular.module('foodProductApp', []);
foodProductApp.run(function($http){
   $http.get("/js/foodProducts.json").then(function(data){
       model.products = data.data.products;
   });
 
});
foodProductApp.filter("checkedProducts", function(){
   return function (products, showEaten){
       var resultArr = [];
       angular.forEach(products, function(product){
           if(product.eaten == false || showEaten == true){
               resultArr.push(product)
           }
       });
       return resultArr;
   } 
});


foodProductApp.controller('foodProductCtrl', function ($scope) {
    $scope.test = model;
    $scope.foodProducts = model;
    $scope.notEatenCount = function(){
       var count = 0;
       angular.forEach($scope.foodProducts.products, function(product){
           if(!product.eaten) {count++}
       });
       return count;
    }
    $scope.warningLevel = function (){
        return $scope.notEatenCount() < 2 ? "label-success" : "label-warning";
    }
    $scope.addNewProduct = function (name, calories){
        $scope.foodProducts.products.push({name: name, calories: calories, eaten: false})
    }
});