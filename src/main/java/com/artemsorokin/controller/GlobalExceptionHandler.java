package com.artemsorokin.controller;

import com.artemsorokin.exception.F1BetException;
import com.artemsorokin.model.ErrorResponse;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ErrorResponse> handleFeignException(
      FeignException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.valueOf(ex.status());

    ErrorResponse errorResponse =
        getErrorResponse(status, ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, HttpServletRequest request) {
    HttpStatus status =
        HttpStatus.INTERNAL_SERVER_ERROR;
    ErrorResponse errorResponse =
        getErrorResponse(
            status, "An unexpected error occurred: " + ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(F1BetException.class)
  public ResponseEntity<ErrorResponse> handleBetException(
      FeignException ex, HttpServletRequest request) {
    HttpStatus status = HttpStatus.BAD_REQUEST;

    ErrorResponse errorResponse =
        getErrorResponse(status, ex.getMessage(), request.getRequestURI());
    return new ResponseEntity<>(errorResponse, status);
  }

  private ErrorResponse getErrorResponse(HttpStatus status, String message, String path) {
    return ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(path)
        .build();
  }
}
