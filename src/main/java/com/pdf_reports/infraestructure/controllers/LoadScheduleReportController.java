package com.pdf_reports.infraestructure.controllers;

import com.pdf_reports.application.services.ILoadScheduleService;
import com.pdf_reports.domain.models.dto.request.LoadScheduleRequest;
import com.pdf_reports.domain.models.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/programa-cargas")
@RestController
public class LoadScheduleReportController {
    private final ILoadScheduleService service;

    @PostMapping("/validar")
    public ResponseEntity<Response<Void>> validate(@RequestBody LoadScheduleRequest request) {
        service.ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.status());

        var response = new Response<Void>(
                true,
                "Validación realizada correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<byte[]> generateReport(@RequestBody LoadScheduleRequest request) {
        byte[] pdfBytes = service.generateReport(request);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(pdfBytes);
    }
}