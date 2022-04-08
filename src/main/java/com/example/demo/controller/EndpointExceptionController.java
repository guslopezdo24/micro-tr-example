package com.example.demo.controller;

import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;

@ControllerAdvice
public class EndpointExceptionController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @ExceptionHandler(value = TimeoutException.class)
    public ResponseEntity<Object> handleTimeoutexception(TimeoutException exception) {

        log.error("Timeout error from resilience4j");
        return new ResponseEntity<>("Timeout", HttpStatus.SERVICE_UNAVAILABLE);
    }


    @ExceptionHandler(value = CallNotPermittedException.class)
    public ResponseEntity<Object> handleCallNotPermittedException(CallNotPermittedException exception) {

        log.error("Circuit open error from resilience4j");
        return new ResponseEntity<>("CB open", HttpStatus.SERVICE_UNAVAILABLE);
    }
}
