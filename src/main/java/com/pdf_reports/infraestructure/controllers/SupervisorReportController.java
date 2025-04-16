package com.pdf_reports.infraestructure.controllers;

import com.pdf_reports.application.services.ISupervisorService;
import com.pdf_reports.domain.models.dto.request.SupervisorRequest;
import com.pdf_reports.domain.models.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/cargas-supervisor")
@RequiredArgsConstructor
public class SupervisorReportController {
    private final ISupervisorService service;

    @PostMapping
    public ResponseEntity<byte[]> getSupervisorReport(@RequestBody SupervisorRequest request) {
        byte[] pdfBytes = service.generateSupervisorReport(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=reporte.pdf");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .body(pdfBytes);
    }

    @PostMapping("/validar")
    public ResponseEntity<Response<Void>> validateSupervisorReport(@RequestBody SupervisorRequest request) {
        service.ensureAnyCargoExists(request);

        var response = new Response<Void>(
                true,
                "Validación realizada correctamente",
                null
        );

        return ResponseEntity.ok(response);
    }
}