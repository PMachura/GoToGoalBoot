/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// OPCJE - ZMIENIĆ transformRequest ale to jest słabe to trzeba zmieniać wszystko ;p  | ZROBIĆ TAK BY KONKRETNE FOOD PRODUCTY BEZ LINKÓW BYŁY RÓWNIEŻ RESOURCAMI
angular.module("goToGoalModule")
        .controller("workoutController", function ($scope, $rootScope,
                workoutResourceHandlerFactory, authenticationHandler , $location) {

           
            $scope.currentWorkoutDay = {};
            var workoutResourceHandler = null;
            $rootScope.displayMode.setView();
            $rootScope.alerter.setDefault();


            var findIndexInArrayById = function (id, array) {
                for (var i = 0; i < array.length; i++) {
                    if (id == array[i].id) {
                        return i;
                    }
                }
                return null;
            };



            $scope.getWorkoutDaysPage = function (page, date) {
                var params = {};
                if (page && angular.isNumber(page)) {
                    params.page = page;
                }
                if (date){
                    params.date = date;
                }
                workoutResourceHandler.getWorkoutDaysPageRequest(params).$promise.then(function (workoutDaysPage) {
                    $scope.workoutDays = workoutDaysPage;
                    $scope.workoutDays.content = workoutResourceHandler.mapWorkoutDaysToResource(workoutDaysPage.content);
                    for (var i = 0; i < $scope.workoutDays.content.length; i++) {
                        workoutResourceHandler.getWorkoutsSetItInWorkoutDay($scope.workoutDays.content[i]);
                    }
                });
            };


            authenticationHandler.getLoggedUserRequest().then(function (response) {
                workoutResourceHandler = workoutResourceHandlerFactory.instantiate(response.data);
                workoutResourceHandler.getExercisesRequest().$promise.then(function (exercises) {
                    $scope.exercises = exercises;
                });
                $scope.getWorkoutDaysPage();
            });




            $scope.createOrEditWorkoutDay = function (workoutDay) {
                if (workoutDay) {
                    $scope.currentWorkoutDay.content = angular.copy(workoutDay);
                } else {
                    $scope.currentWorkoutDay.content = workoutResourceHandler.getEmptyWorkoutDay();
                }
                $rootScope.alerter.hide();
                $rootScope.displayMode.setCreator();
            };

            $scope.addExerciseToWorkout = function (exercise, workout) {
                if (angular.isObject(exercise) && angular.isObject(workout)) {
                    var doneExercise = angular.copy(exercise);
                    workoutResourceHandler.setDefaultParametersInDoneExercise(doneExercise);
                    workout.doneExercises.push(doneExercise);
                }
            };

            $scope.addEmptyWorkoutToWorkoutDay = function (workoutDay) {
                workoutDay.workouts.push(workoutResourceHandler.getEmptyWorkout());
            };
            

            $scope.deleteDoneExerciseFromWorkout = function (doneExercise, workout) {
                workout.doneExercises.splice(workout.doneExercises.indexOf(doneExercise), 1);
            };

            $scope.deleteWorkoutFromWorkoutDay = function (workoutDay, workout) {
                workoutDay.workouts.splice(workoutDay.meals.indexOf(workout), 1);
            };


            $scope.saveCurrentWorkoutDay = function (currentWorkoutDay) {
                if (angular.isDefined(currentWorkoutDay.content.id)) {
                    $scope.updateWorkoutDay(currentWorkoutDay.content);
                } else {
                    $scope.createWorkoutDay(currentWorkoutDay.content);
                }
            };

            $scope.deleteWorkoutDay = function (workoutDay) {
                workoutResourceHandler.deleteWorkoutDayRequest(workoutDay).$promise.then(function () {
                    var index = findIndexInArrayById(workoutDay.id, $scope.workoutDays.content);
                    if (angular.isNumber(index)) {
                        $scope.workoutDays.content.splice(index, 1);
                    }
                    $rootScope.alerter.setSuccessMessage("Dziennik treningowy został usunięty");
                    $rootScope.displayMode.setView();
                    $location.path("/workoutDays");
                });
            };

            $scope.updateWorkoutDay = function (workoutDay) {
                workoutResourceHandler.updateWorkoutDayRequest(workoutDay).$promise.then(function (workoutDayResponse) {
                    var index = findIndexInArrayById(workoutDayResponse.id, $scope.workoutDays.content);
                    if (angular.isNumber(index)) {
                        $scope.workoutDays.content[index] = workoutDayResponse;
                    }
                    $rootScope.alerter.setSuccessMessage("Dziennik treningowy został zaktualizowany");
                    $rootScope.displayMode.setView();
                    $location.path("/workoutDays");
                }, function (error) {
                    $rootScope.alerter.setWarnintMessage(error.data.message);
                    $location.path("/workoutDays");
                });

            };

            $scope.createWorkoutDay = function (workoutDay) {
                workoutResourceHandler.createWorkoutDayRequest(workoutDay).$promise.then(function (workoutDayResponse) {
                    $scope.workoutDays.content.push(workoutDayResponse);
                    $rootScope.alerter.setSuccessMessage("Dziennik treningowy został zapisany");
                    $rootScope.displayMode.setView();
                    $location.path("/workoutDays");
                }, function (error) {
                    $rootScope.alerter.setWarnintMessage(error.data.message);
                    $location.path("/workoutDays");
                });
            };
            
            
            $scope.cancelWorkoutDayCreation = function(currentWorkoutDay){
                currentWorkoutDay={};
                $rootScope.displayMode.setView();
            };
            
            
        });

