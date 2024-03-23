package com.springbootangularshop.springbootbackend.system.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, String id) {
        super("Could not find " + objectName + " with Id " + id);
    }

    public ObjectNotFoundException(String objectName, Long id) {
        super("Could not find " + objectName + " with Id " + id);
    }

    public ObjectNotFoundException(String objectName1, String objectName2, String id) {
        super("Could not find " + objectName1 + " with " + objectName2 + " Id " + id);
    }
}