/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Przemek
 */
@ControllerAdvice
public class ExceptionControllerAdvisor {
   
    @ExceptionHandler(EntityDuplicateException.class)
    public ResponseEntity entityDuplicateHandle(EntityDuplicateException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    } 
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity entityNotFoundHandle(EntityNotFoundException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    } 
    
    @ExceptionHandler(ForbiddenResourcesException.class)
    public ResponseEntity forbiddenResourcesHandle(ForbiddenResourcesException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    } 
    
    @ExceptionHandler(DataFormatException.class)
    public ResponseEntity dateFormatHandle(DataFormatException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    } 
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity unauthorizedHandle(UnauthorizedException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    } 
    
    @ExceptionHandler(ForbiddenDataUpdateException.class)
    public ResponseEntity forbiddenDataUpdateHandle(ForbiddenDataUpdateException exception){
        return new ResponseEntity(exception, HttpStatus.CONFLICT);
    } 
}


