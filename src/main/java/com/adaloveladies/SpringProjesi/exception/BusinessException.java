package com.adaloveladies.SpringProjesi.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(HttpStatus.BAD_REQUEST, message, "BUSINESS_ERROR");
    }

    public BusinessException(String message, String errorCode) {
        super(HttpStatus.BAD_REQUEST, message, errorCode);
    }
} 