package com.trimv.base.endpoint.error;

import com.trimv.base.endpoint.exception.GeneralException;
import com.trimv.base.model.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@ControllerAdvice
@Slf4j
public class GeneralExceptionHandling {


    @ExceptionHandler({GeneralException.class})
    public ResponseEntity<ErrorResponse> globalGeneralExceptionHandler(GeneralException ex, HttpServletRequest request) {
        ErrorResponse message = new ErrorResponse(request.getServletPath(), request.getMethod(), ex.getErrorCode(), ex.getMessage(), 400);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception ex, HttpServletRequest request) {
        ErrorResponse message = new ErrorResponse(request.getServletPath(), request.getMethod(), "", ex.getMessage(), 500);
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ErrorResponse message = new ErrorResponse(request.getServletPath(), request.getMethod(),errors ,"VALIDATE", 400);
        return ResponseEntity.ok(message);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({NoHandlerFoundException.class})
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpServletRequest request) {
        ErrorResponse message = new ErrorResponse(request.getServletPath(), request.getMethod(), "URI_NOT_FOUND","Resource not found" , 404);
        return ResponseEntity.ok(message);
    }
}
