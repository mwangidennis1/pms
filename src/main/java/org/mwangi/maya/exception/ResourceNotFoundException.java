package org.mwangi.maya.exception;

public class ResourceNotFoundException extends Exception{
    public ResourceNotFoundException(String message){
        super(message);
    }
    public ResourceNotFoundException(){
        super("resource not found");
    }
}
