package com.pdf_reports.domain.models.dto.request;

public record ReportRequest(
        String cd,
        String dispatchDate,
        int cargoNumber
) {}