package com.pdf_reports.infraestructure.controllers;

import com.pdf_reports.application.services.IGeneralCargoService;
import com.pdf_reports.domain.models.dto.request.GeneralCargoRequest;
import com.pdf_reports.domain.models.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/cargas-general")
@RequiredArgsConstructor
public class GeneralCargoReportController {
    private final IGeneralCargoService service;

    @PostMapping
    public ResponseEntity<byte[]> generateGeneralCargoReport(@RequestBody GeneralCargoRequest request) {
        byte[] pdfBytes = service.generateGeneralCargoReport(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validate(@RequestBody GeneralCargoRequest request) {
        service.ensureAnyCargoExists(request);

        var response = new Response<Void>(
                true,
                "Validación realizada correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }
}