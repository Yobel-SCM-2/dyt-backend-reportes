package com.pdf_reports.domain.repositories;

import com.pdf_reports.domain.models.dto.response.ReportHeader;
import com.pdf_reports.domain.models.dto.response.ReportParameters;
import com.pdf_reports.domain.models.dto.response.ReportSummary;

import java.time.LocalDateTime;

public interface ISupervisorRepository {
    int validate(String account, LocalDateTime processDate, int processBatch);
    ReportParameters getReportParameters(String account);
    ReportHeader getReportHeader(String account, LocalDateTime processDate, int processBatch);
    ReportSummary getReportSummary(String cd, String account, LocalDateTime processDate, int processBatch);
}