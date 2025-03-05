package com.pdf_reports.infraestructure.exceptionHandlers;

import com.pdf_reports.domain.models.dto.response.ErrorResponse;
import com.pdf_reports.utils.ErrorFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> badRequestExceptionHandler(IllegalArgumentException exception) {
        return ErrorFactory.createErrorResponse(exception.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> databaseAccessExceptionHandler(DataAccessException exception) {
        return ErrorFactory.createErrorResponse(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, null);
    }
}