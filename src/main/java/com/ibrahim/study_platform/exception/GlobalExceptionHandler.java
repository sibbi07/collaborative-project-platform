package com.ibrahim.study_platform.exception;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import java.util.List;


@RestControllerAdvice 
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception){
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        FieldError error = fieldErrors.get(0);
        String message = error.getDefaultMessage();
        return ResponseEntity.badRequest().body(message);
    }

}
