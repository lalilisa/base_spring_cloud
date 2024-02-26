package com.trimv.base.endpoint.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GeneralException extends RuntimeException {

    private String errorCode;

    public GeneralException(String errorCode,String message){
        super(message);
        this.errorCode = errorCode;
    }

    public GeneralException(String message){
        super(message);
    }
}
