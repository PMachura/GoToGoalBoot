/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.rest.controller.workout;

import gotogoal.model.workout.WorkoutDay;
import gotogoal.rest.resource.assembler.workout.WorkoutDayResourceAssembler;
import gotogoal.rest.resource.nutrition.NutritionDayResource;
import gotogoal.rest.resource.workout.WorkoutDayResource;
import gotogoal.service.workout.WorkoutDayService;
import java.security.Principal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Przemek
 */
@RestController
@RequestMapping("/api/users/{userId}/workoutDays")
public class WorkoutDayRestController {

    WorkoutDayService workoutDayService;
    WorkoutDayResourceAssembler workoutDayResourceAssembler;

    @Autowired
    public WorkoutDayRestController(WorkoutDayService workoutDayService, WorkoutDayResourceAssembler workoutDayResourceAssembler) {
        this.workoutDayService = workoutDayService;
        this.workoutDayResourceAssembler = workoutDayResourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<WorkoutDayResource>> findAll(
            @PathVariable Long userId,
            @RequestParam(value = "date", required = false, defaultValue = "2050-01-01") LocalDate date,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size,
            @RequestParam(value = "sort", required = false, defaultValue = "desc") String sort) {

        Page<WorkoutDayResource> workoutDayResourcePage
                = workoutDayResourceAssembler.toResource(workoutDayService.findByUserIdAndDateLessThanEqualEager(userId, date, new PageRequest(page, size, new Sort(Sort.Direction.fromStringOrNull(sort), "date"))));

        return new ResponseEntity(workoutDayResourcePage, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<WorkoutDayResource> findOne(@PathVariable Long id){
        return new ResponseEntity(workoutDayResourceAssembler.toResource(workoutDayService.findOneEagerDeep(id)), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long userId, @PathVariable Long id, Principal principal) {
        workoutDayService.delete(userId, id, principal);
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<WorkoutDayResource> create(@RequestBody WorkoutDay workoutDay, @PathVariable Long userId, Principal principal) {
        WorkoutDay created = workoutDayService.create(workoutDay, userId, principal);
        WorkoutDayResource response = workoutDayResourceAssembler.toResource(workoutDayService.findOneEagerDeep(created.getId()));
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<WorkoutDayResource> update(@RequestBody WorkoutDay workoutDay,
            @PathVariable Long userId,
            @PathVariable Long id,
            Principal principal){
        
        WorkoutDay updated = workoutDayService.update(workoutDay, userId, id, principal);
        WorkoutDayResource response = workoutDayResourceAssembler.toResource(workoutDayService.findOneEagerDeep(updated.getId()));
        return new ResponseEntity(response, HttpStatus.OK);
        
    }
    
    
}
