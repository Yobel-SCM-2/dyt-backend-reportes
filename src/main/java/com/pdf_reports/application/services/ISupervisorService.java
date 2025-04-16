package com.pdf_reports.application.services;

import com.pdf_reports.domain.models.dto.request.SupervisorRequest;

public interface ISupervisorService {
    byte[] generateSupervisorReport(SupervisorRequest request);
    void ensureAnyCargoExists(SupervisorRequest request);
}