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

public class EntityRelationException extends RuntimeException {
    public  EntityRelationException(String message){
        super(message);
    }
    
    public EntityRelationException(String message, Throwable cause){
        super(message,cause);
    }
}