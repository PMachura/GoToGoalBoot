/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.exception;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Przemek
 */
@ControllerAdvice
public class ConversionFailedExceptionAdvisor {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "ConversionFailedException")
    public void handleConversionFailedException() {

    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT, reason = "ConversionFailedException")
    public void handleHttpMessageNotReadableException() {

    }
}
