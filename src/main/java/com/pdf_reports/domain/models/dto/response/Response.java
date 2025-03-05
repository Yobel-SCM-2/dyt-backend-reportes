package com.pdf_reports.domain.models.dto.response;

public record Response<T>(
        boolean success,
        String message,
        T data
) {}