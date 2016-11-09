/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("securityModule", ['ngRoute'])
        .constant("foodProductsDataUrl", "http://localhost:8080/api/foodProducts")
        .constant("userPrincipalUrl", "http://localhost:8080/api/users/principal")
        .constant("logoutUrl","http://localhost:8080/logout")
        .config(function ($routeProvider, $httpProvider) {

            $routeProvider.when('/', {
                templateUrl: "/templates/angularSecurity/home.html",
                controller: 'home'
            }).when('/login', {
                templateUrl: "/templates/angularSecurity/login.html",
                controller: 'navigation'
            }).otherwise('/');

            $httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';

        })
        .controller('home', function ($http, $scope, foodProductsDataUrl) {
            $scope.greeting = "Greeting initial";
            $scope.getData = function () {
                $http.get(foodProductsDataUrl).then(function (response) {
                    $scope.greeting = response.data;
                }, function (response) {
                    $scope.greeting = "Content not avaliable";
                });
            };
        })
        .controller('navigation',
                function ($rootScope, $scope, $http, $location, userPrincipalUrl, logoutUrl) {

                    var authenticate = function (credentials, callback) {

                        var headers = credentials ? {authorization: "Basic "
                                    + btoa(credentials.username + ":" + credentials.password)
                        } : {};

                        $http.get(userPrincipalUrl, {headers: headers}).then(function (response) {
                            if (response.data.name) {
                                $rootScope.authenticated = true;
                            } else {
                                $rootScope.authenticated = false;
                            }
                            callback && callback();
                        }, function () {
                            $rootScope.authenticated = false;
                            callback && callback();
                        });

                    };

                    authenticate();
                    $scope.credentials = {};
                    $scope.login = function () {
                        authenticate($scope.credentials, function () {
                            if ($rootScope.authenticated) {
                                $location.path("/");
                                $scope.error = false;
                            } else {
                                $location.path("/login");
                                $scope.error = true;
                            }
                        });
                    };

                    $scope.logout = function () {
                        $http.post(logoutUrl, {}).finally(function () {
                            $rootScope.authenticated = false;
                            $location.path("/");
                        });
                    };
                });

