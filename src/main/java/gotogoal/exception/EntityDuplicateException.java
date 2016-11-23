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

public class EntityDuplicateException extends RuntimeException {
    public  EntityDuplicateException(String message){
        super(message);
    }
    
    public EntityDuplicateException(String message, Throwable cause){
        super(message,cause);
    }
}