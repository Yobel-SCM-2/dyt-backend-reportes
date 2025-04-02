package com.pdf_reports.application.services;

import com.pdf_reports.domain.models.dto.request.DispatchControlRequest;

public interface IRouteControlService {
    byte[] generateRouteControlReport(DispatchControlRequest request);
    void ensureAnyCargoExists(String cd, String dispatchDate, int cargoNumber);
}