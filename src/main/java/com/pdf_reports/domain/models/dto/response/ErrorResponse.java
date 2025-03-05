package com.pdf_reports.domain.models.dto.response;

public record ErrorResponse(
        boolean success,
        String message,
        String error
) {}