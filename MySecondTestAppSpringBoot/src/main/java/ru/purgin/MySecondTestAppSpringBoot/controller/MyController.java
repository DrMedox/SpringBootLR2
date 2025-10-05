package ru.purgin.MySecondTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import ru.purgin.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.purgin.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.purgin.MySecondTestAppSpringBoot.model.Request;
import ru.purgin.MySecondTestAppSpringBoot.model.Response;
import ru.purgin.MySecondTestAppSpringBoot.service.ValidationService;

@Slf4j
@RestController
public class MyController {

    private final ValidationService validationService;

    public MyController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        try {

            validationService.isValid(bindingResult);

            //uid == "123" → UnsupportedCodeException
            if ("123".equals(request.getUid())) {
                throw new UnsupportedCodeException("uid 123 не поддерживается");
            }
            Response response = Response.builder()
                    .uid(request.getUid())
                    .operationUid(request.getOperationUid())
                    .systemTime(request.getSystemTime())
                    .code("success")
                    .errorCode("")
                    .errorMessage("")
                    .build();

            return ResponseEntity.ok(response);

        } catch (ValidationFailedException e) {
            Response response = Response.builder()
                    .code("failed")
                    .errorCode("ValidationException")
                    .errorMessage(e.getMessage() == null ? "Ошибка валидации" : e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (UnsupportedCodeException e) {
            Response response = Response.builder()
                    .code("failed")
                    .errorCode("UnsupportedCodeException")
                    .errorMessage(e.getMessage())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            log.error("Unknown error", e);
            Response response = Response.builder()
                    .code("failed")
                    .errorCode("UnknownException")
                    .errorMessage("Произошла непредвиденная ошибка")
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}