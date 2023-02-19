package com.dzhatdoev.myinterviewtask.util;

import com.dzhatdoev.myinterviewtask.util.exceptions.PersonNotCreatedException;
import com.dzhatdoev.myinterviewtask.util.exceptions.PersonNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionControllerHandler {

    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PersonErrorResponse onConstraintValidationException(ConstraintViolationException e) {
        return new PersonErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PersonErrorResponse onCMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return new PersonErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseBody
    @ExceptionHandler(PersonNotCreatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public PersonErrorResponse onPersonNotCreatedException(PersonNotCreatedException e) {
        return new PersonErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public PersonErrorResponse onAccessDeniedException(AccessDeniedException e) {
        return new PersonErrorResponse(e.getMessage(), LocalDateTime.now());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PersonNotFoundException.class)
    private PersonErrorResponse handleException(PersonNotFoundException e) {
        return new PersonErrorResponse(e.getMessage(), LocalDateTime.now());
    }

//    @ResponseBody
//    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
//    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
//    private PersonErrorResponse handleException(HttpMediaTypeNotSupportedException e) {
//        return new PersonErrorResponse(e.getMessage(),LocalDateTime.now());
//    }


}
