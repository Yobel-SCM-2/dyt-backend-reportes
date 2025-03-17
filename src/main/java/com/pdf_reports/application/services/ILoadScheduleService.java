package com.pdf_reports.application.services;

import com.pdf_reports.domain.models.dto.request.LoadScheduleRequest;

public interface ILoadScheduleService {
    byte[] generateReport(LoadScheduleRequest request);
    void ensureAnyCargoExists(String cd, String dispatchDate, String status);
}