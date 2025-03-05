package com.pdf_reports.utils;

import com.pdf_reports.domain.models.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorFactory {
    public static ResponseEntity<ErrorResponse> createErrorResponse(String message, HttpStatus status, String errorDetails) {
        var response = new ErrorResponse(false, message, errorDetails);

        return new ResponseEntity<>(response, status);
    }
}