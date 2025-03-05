package com.pdf_reports.infraestructure.exceptionHandlers;

import com.pdf_reports.domain.models.dto.response.ErrorResponse;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DispatchControlReportExceptionHandler {
    @ExceptionHandler(CargoDoesNotExistException.class)
    public ResponseEntity<ErrorResponse> cargoDoesNotExistExceptionHandler(CargoDoesNotExistException exception) {
        var response = new ErrorResponse(false, exception.getMessage(), HttpStatus.BAD_REQUEST.name());
        return ResponseEntity.badRequest().body(response);
    }
}