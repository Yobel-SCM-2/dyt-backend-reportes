package com.pdf_reports.infraestructure.exceptionHandlers;

import com.pdf_reports.domain.models.dto.response.ErrorResponse;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import com.pdf_reports.utils.exceptions.CargoStatusDoesNotExist;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ReportsExceptionHandler {
    @ExceptionHandler(CargoDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> cargoDoesNotExistExceptionHandler(CargoDoesNotExistException exception) {
        var response = new ErrorResponse(false, exception.getMessage(), HttpStatus.BAD_REQUEST.name());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(CargoStatusDoesNotExist.class)
    public ResponseEntity<ErrorResponse> cargoStatusDoesNotExistExceptionHandler(CargoStatusDoesNotExist exception) {
        var response = new ErrorResponse(false, exception.getMessage(), HttpStatus.BAD_REQUEST.name());
        return ResponseEntity.badRequest().body(response);
    }
}