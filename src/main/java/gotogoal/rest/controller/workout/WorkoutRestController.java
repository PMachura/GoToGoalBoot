/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.workout;

import gotogoal.model.workout.Workout;
import gotogoal.rest.resource.assembler.workout.WorkoutResourceAssembler;
import gotogoal.rest.resource.workout.WorkoutDayResource;
import gotogoal.rest.resource.workout.WorkoutResource;
import gotogoal.service.workout.WorkoutService;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/users/{userId}/workoutDays/{workoutDayId}/workouts")
public class WorkoutRestController {
    
    WorkoutService workoutService;
    WorkoutResourceAssembler workoutResourceAssembler;
    
    @Autowired
    public WorkoutRestController(WorkoutService workoutService, WorkoutResourceAssembler workoutResourceAssembler) {
        this.workoutService = workoutService;
        this.workoutResourceAssembler = workoutResourceAssembler;
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<WorkoutResource> findOne(@PathVariable Long id){
        return new ResponseEntity(workoutResourceAssembler.toResource(workoutService.findOneEager(id)), HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<WorkoutResource>> findAll(@PathVariable Long userId, @PathVariable Long workoutDayId){
       Collection<WorkoutResource> workoutsResources = workoutResourceAssembler.toResource
        (workoutService.findByUserIdAndWorkoutDayIdEager(userId, workoutDayId));
       
       return new ResponseEntity(workoutsResources, HttpStatus.OK);
    }
    
    
}
