package com.example.blooddonor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalErrorController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // print full error in console for debugging
        return new ResponseEntity<>("⚠️ Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
