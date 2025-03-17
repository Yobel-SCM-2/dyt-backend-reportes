package com.pdf_reports.domain.models.dto.request;

public record LoadScheduleRequest(
        String cd,
        String dispatchDate,
        String status,
        String time
) {}
