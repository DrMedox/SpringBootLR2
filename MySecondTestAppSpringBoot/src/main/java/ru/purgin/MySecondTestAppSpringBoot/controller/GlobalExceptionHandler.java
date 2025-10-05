package ru.purgin.MySecondTestAppSpringBoot.controller;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import ru.purgin.MySecondTestAppSpringBoot.exception.*;
import ru.purgin.MySecondTestAppSpringBoot.model.Response;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationFailedException.class)
    public ResponseEntity<Response> handleValidationFailed(ValidationFailedException ex) {
        Response resp = Response.builder()
                .code("failed")
                .errorCode("ValidationException")
                .errorMessage(ex.getMessage() == null ? "Ошибка валидации" : ex.getMessage())
                .build();
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response> handleMethodArgNotValid(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        Response resp = Response.builder()
                .code("failed")
                .errorCode("ValidationException")
                .errorMessage(msg)
                .build();
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    //uid == 123
    @ExceptionHandler(UnsupportedCodeException.class)
    public ResponseEntity<Response> handleUnsupported(UnsupportedCodeException ex) {
        Response resp = Response.builder()
                .code("failed")
                .errorCode("UnsupportedCodeException")
                .errorMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleUnknown(Exception ex) {
        Response resp = Response.builder()
                .code("failed")
                .errorCode("UnknownException")
                .errorMessage("Произошла непредвиденная ошибка")
                .build();
        return new ResponseEntity<>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

