/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module("resourceHandlerModule")
        .factory("workoutResourceHandlerFactory", function ($resource) {
            var resourceUrlPrefix = "http://localhost:8080/api";
            var mapToResource = function (data, resourceClass, propertyName) {
                if (angular.isDefined(propertyName)) {
                    for (var i = 0; i < data.length; i++) {
                        if (data[i].hasOwnProperty(propertyName)) {
                            data[i][propertyName] = new resourceClass(data[i][propertyName]);
                        }

                    }
                } else {
                    for (var i = 0; i < data.length; i++) {
                        data[i] = new resourceClass(data[i]);
                    }
                }
                return data;
            };

            var instantiate = function (user) {
                var workoutResourceHandler = new Object();
                workoutResourceHandler.user = angular.copy(user);
                workoutResourceHandler.mapToResource = mapToResource;
                workoutResourceHandler.exercisesResource = $resource(resourceUrlPrefix + "/exercises/:id", {id: "@id"});
                workoutResourceHandler.workoutsResource = $resource(resourceUrlPrefix + "/users/:userId/workoutDays/:workoutDayId/workouts/:id",
                        {userId: user.id, workoutDayId: "workoutDay.id", id: "@id"},
                        {
                            create: {
                                method: "POST"
                            },
                            save: {
                                method: "PUT"
                            }
                        });
                workoutResourceHandler.workoutDaysResource = $resource(resourceUrlPrefix + "/users/:userId/workoutDays/:id", {userId: user.id, id: "@id"}, {
                    query: {
                        isArray: false
                    },
                    create: {
                        method: "POST"
                    },
                    save: {
                        method: "PUT"
                    }
                });
                workoutResourceHandler.mapWorkoutDaysToResource = function(workoutDays){
                    return this.mapToResource(workoutDays, this.workoutDaysResource);
                };
                workoutResourceHandler.getEmptyWorkoutDay = function() {
                    return {
                        date: "2016-11-11",
                        workouts: []
                    };
                };
                workoutResourceHandler.getEmptyWorkout = function () {
                    return {
                        doneExercises: [],
                        time: "00:00:00",
                        startTime: "12:00:00"
                    };
                },
                
                workoutResourceHandler.getExercisesRequest = function () {
                    return this.exercisesResource.query();
                },
                workoutResourceHandler.getWorkoutsForWorkoutDayRequest = function (workoutDay) {
                    params = {workoutDayId: workoutDay.id};
                    return this.workoutsResource.query(params);
                },
                workoutResourceHandler.getWorkoutsSetItInWorkoutDay = function (workoutDay) {
                    this.getWorkoutsForWorkoutDayRequest(workoutDay).$promise.then(function (workouts) {
                        workoutDay.workouts = workouts;
                    });
                },
                workoutResourceHandler.getWorkoutDaysPageRequest = function (params) {
                    return this.workoutDaysResource.query(params);
                },
                workoutResourceHandler.createWorkoutDayRequest = function (workoutDay) {
                    return this.workoutDaysResource.create(workoutDay);
                },
                workoutResourceHandler.updateWorkoutDayRequest = function (workoutDay) {
                    return this.workoutDaysResource.save(workoutDay);
                },
                workoutResourceHandler.deleteWorkoutDayRequest =  function (workoutDay) {
                    return this.workoutDaysResource.delete(workoutDay);
                };
                workoutResourceHandler.setDefaultParametersInDoneExercise = function(exercise){
                    exercise.charge = 0;
                    exercise.repeats = 0;
                    exercise.series = 0;
                    exercise.intensity =0;
                    exercise.time = "00:00:00";
                    
                };
                return workoutResourceHandler;
            };
           
            return {
               instantiate: instantiate
            };

        });

