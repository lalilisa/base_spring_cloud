package com.trimv.base.model.error;


import lombok.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ErrorResponse {

    private Date timestamp;
    private String path;
    private Map<String, Object> error = new HashMap<>();
    private String method;
    private String errorCode;
    private Integer status;

    public ErrorResponse(String path, String method, String errorCode, String message, Integer status) {
        this.timestamp = new Date();
        this.path = path;
        this.errorCode = errorCode;
        this.method = method;
        this.error.put("message", message);
        this.status = status;

    }

    public ErrorResponse(String path, String method,  Map<String, Object> error,String errorCode, Integer status) {
        this.timestamp = new Date();
        this.path = path;
        this.errorCode = errorCode;
        this.method = method;
        this.error = error;
        this.status = status;
    }
}
