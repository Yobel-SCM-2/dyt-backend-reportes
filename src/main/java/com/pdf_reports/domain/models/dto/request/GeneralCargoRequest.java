package com.pdf_reports.domain.models.dto.request;

import java.time.OffsetDateTime;

public record GeneralCargoRequest(
        String account,
        OffsetDateTime processDate,
        int processBatch,
        String timeZone,
        String time,
        int region,
        String lpr
) {}
