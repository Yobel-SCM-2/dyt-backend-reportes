package com.pdf_reports.domain.models.dto.response;

public record ReportParameters(
        String account,
        String accountDesc,
        String cd,
        String cdDesc
) {}