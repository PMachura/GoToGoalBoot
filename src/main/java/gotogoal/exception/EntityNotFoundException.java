/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gotogoal.exception;

/**
 *
 * @author Przemek
 */
public class EntityNotFoundException extends Exception {
    public  EntityNotFoundException(String message){
        super(message);
    }
    
    public EntityNotFoundException(String message, Throwable cause){
        super(message,cause);
    }
}
