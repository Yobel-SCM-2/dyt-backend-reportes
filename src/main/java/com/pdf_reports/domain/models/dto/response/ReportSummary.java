package com.pdf_reports.domain.models.dto.response;

public record ReportSummary(
        int totalOrder,
        int totalAgp,
        int normalOrders
) {}