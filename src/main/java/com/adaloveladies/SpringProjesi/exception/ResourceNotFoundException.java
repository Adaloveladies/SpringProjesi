package com.adaloveladies.SpringProjesi.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s bulunamadı: %s = '%s'", resourceName, fieldName, fieldValue));
    }
} 