package com.pdf_reports.application.services;

import com.pdf_reports.domain.models.dto.request.GeneralCargoRequest;

public interface IGeneralCargoService {
    byte[] generateGeneralCargoReport(GeneralCargoRequest request);
    void ensureAnyCargoExists(GeneralCargoRequest request);
}