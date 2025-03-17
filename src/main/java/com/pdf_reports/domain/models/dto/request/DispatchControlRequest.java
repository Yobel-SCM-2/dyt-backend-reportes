package com.pdf_reports.domain.models.dto.request;

public record DispatchControlRequest(
        String cd,
        String dispatchDate,
        int cargoNumber,
        String time
) {}