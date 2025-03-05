package com.pdf_reports.application.services;

import com.pdf_reports.domain.models.dto.request.ReportRequest;
import net.sf.jasperreports.engine.JRException;

import java.io.IOException;

public interface IDispatchControlReportService {
    byte[] generateDispatchControlReport(ReportRequest request);
    void ensureAnyCargoExists(String cd, String dispatchDate, int cargoNumber);
}