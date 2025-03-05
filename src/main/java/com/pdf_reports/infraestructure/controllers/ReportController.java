package com.pdf_reports.infraestructure.controllers;

import com.pdf_reports.application.services.IDispatchControlReportService;
import com.pdf_reports.domain.models.dto.request.ReportRequest;
import com.pdf_reports.domain.models.dto.response.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/control-despacho")
public class ReportController {
    private final IDispatchControlReportService service;

    public ReportController(IDispatchControlReportService service) {
        this.service = service;
    }

    @PostMapping("/validar")
    public ResponseEntity<Response<Void>> validateReportDataExistence(@RequestBody ReportRequest request) {
        service.ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.cargoNumber());
        var response = new Response<Void>(
                true,
                "Validación realizada correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<byte[]> generateDispatchControlReport(@RequestBody ReportRequest request) {
        byte[] pdfBytes = service.generateDispatchControlReport(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(pdfBytes);
    }
}