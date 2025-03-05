package com.pdf_reports.utils;

import com.pdf_reports.domain.models.dto.response.ErrorResponse;
import com.pdf_reports.domain.models.dto.response.Response;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {
    public static<T> ResponseEntity<Response<T>> buildSuccessResponse(T data, String message) {
        var response = new Response<T>(true, message, data);

        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<ErrorResponse> buildFailureResponse(String message) {
        var response = new ErrorResponse(false, message, "");

        return ResponseEntity.badRequest().body(response);
    }
}