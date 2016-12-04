/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("resourceHandlerModule")
        .factory("authenticationHandler", function ($http) {
            var userPrincipalUrl = "http://localhost:8080/api/users/principal";
            var loggedUserDataUtl = "http://localhost:8080/api/users/logged";
            var logoutUrl = "http://localhost:8080/logout";

            var user = null;


            return {
                loginRequest: function (credentials) {
                    var headers = credentials ? {authorization: "Basic " + btoa(credentials.userEmail + ":" + credentials.password)} : {};
                    return  $http.get(userPrincipalUrl, {headers: headers});
                },
                logoutRequest: function () {
                    return $http.post(logoutUrl, {});
                },
                getLoggedUserRequest: function () {
                    return $http.get(loggedUserDataUtl);
                },
                setLoggedUser: function (givenUser) {
                    if ((givenUser)) {
                        user = givenUser;
                    } else {
                        $http.get(loggedUserDataUtl).then(function (response) {
                            user = response.data;
                        }, function(){
                            user = null;
                        });
                    }

                },
                getLoggedUser: function () {
                    return user;
                }
            };

        });
