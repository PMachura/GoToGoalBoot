/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("goToGoal")
        .constant("foodProductListActiveClass", "btn-primary")
        .constant("foodProductListPageCount", 4)
        .controller("foodProductListCtrl", function ($scope, $filter, foodProductListActiveClass,
                foodProductListPageCount, cart) {
            var selectedCategory = null;
            $scope.selectedPage = 1;
            $scope.pageSize = foodProductListPageCount;

            $scope.selectCategory = function (newCategory) {
                selectedCategory = newCategory;
                $scope.selectedPage = 1;
            };
            
            
            $scope.selectPage = function (newPage) {
                $scope.selectedPage = newPage;
            };

            $scope.categoryFilterFunction = function (foodProduct) {
                return selectedCategory == null || foodProduct.category == selectedCategory;
            };

            $scope.getCategoryClass = function (category) {
                return selectedCategory == category ? foodProductListActiveClass : "";
            };
            
            $scope.getPageClass = function(page){
                return $scope.selectedPage == page ? foodProductListActiveClass : "";
            };
            $scope.addProductToCart = function(product){
                cart.addProduct(product.id, product.name, product.calories);//tutaj uwaga warto zmienić kalorię na cenę xD
            };
        });
