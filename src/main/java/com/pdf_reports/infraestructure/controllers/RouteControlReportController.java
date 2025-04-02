package com.pdf_reports.infraestructure.controllers;

import com.pdf_reports.application.services.IRouteControlService;
import com.pdf_reports.domain.models.dto.request.DispatchControlRequest;
import com.pdf_reports.domain.models.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/control-ruta")
@RequiredArgsConstructor
public class RouteControlReportController {
    private final IRouteControlService service;

    @PostMapping("/validar")
    public ResponseEntity<Response<Void>> validateReportDataExistence(@RequestBody DispatchControlRequest request) {
        service.ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.cargoNumber());
        var response = new Response<Void>(
                true,
                "Validación realizada correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<byte[]> generateRouteControlReport(@RequestBody DispatchControlRequest request) {
        byte[] pdfBytes = service.generateRouteControlReport(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(pdfBytes);
    }
}