/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.workout;

import gotogoal.rest.resource.assembler.workout.ExerciseResourceAssembler;
import gotogoal.rest.resource.nutrition.FoodProductResource;
import gotogoal.rest.resource.workout.ExerciseResource;
import gotogoal.service.workout.ExerciseService;
import java.util.Collection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/exercises")
public class ExerciseRestController {
    
    private ExerciseService exerciseService;
    private ExerciseResourceAssembler exerciseResourceAssembler; 

    public ExerciseRestController(ExerciseService exerciseService, ExerciseResourceAssembler exerciseResourceAssembler) {
        this.exerciseService = exerciseService;
        this.exerciseResourceAssembler = exerciseResourceAssembler;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Collection<ExerciseResource>> findAll(){ 
        Collection<ExerciseResource> exerciseResource = exerciseResourceAssembler.toResource(exerciseService.findAll());
         
        return new ResponseEntity(exerciseResource, HttpStatus.OK);
    }
    
}
