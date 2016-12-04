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
public class PathAndModelDataMismatchException extends RuntimeException {
    public  PathAndModelDataMismatchException(String message){
        super(message);
    }
    
    public PathAndModelDataMismatchException(String message, Throwable cause){
        super(message,cause);
    }
}