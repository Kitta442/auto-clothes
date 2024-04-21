package com.grinko.autoclothes.web.addons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@Slf4j
@ControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler
    ResponseEntity<?> handleException(final Exception e) {

        if (e instanceof AccessDeniedException) {
            return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(e.getMessage());
        } else {
            log.error("rest error", e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }

    }
}