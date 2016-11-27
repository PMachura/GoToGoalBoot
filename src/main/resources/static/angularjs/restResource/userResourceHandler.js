/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module("resourceHandlerModule")
        .factory("userResourceHandler", function ($resource) {
            var resourceUrlPrefix = "http://localhost:8080/api";
            var userResource = $resource(resourceUrlPrefix + "/users/:id", {id: "@id"}, {
                create: {
                    method: "POST"
                },
                save: {
                    method: "PUT"
                }
            });

            return {
                createUser: function(user){
                    return userResource.create({}, user);
                }
            };
        });