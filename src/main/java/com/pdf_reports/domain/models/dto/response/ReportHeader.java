package com.pdf_reports.domain.models.dto.response;

import java.util.Date;

public record ReportHeader(
        String cd,
        String campaign,
        Date dispatchDate
) {}