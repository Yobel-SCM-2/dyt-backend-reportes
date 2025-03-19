package com.pdf_reports.infraestructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdf_reports.application.services.ILoadScheduleService;
import com.pdf_reports.domain.models.dto.request.LoadScheduleRequest;
import com.pdf_reports.utils.constants.messages.ErrorMessage;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoadScheduleReportController.class)
public class LoadScheduleReportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ILoadScheduleService service;

    @Nested
    @DisplayName("Pruebas de casos exitosos para generar reportes de programa de carga")
    public class GenerateReportSuccessfullyTest {
        @Test
        @DisplayName("Validar que haya contenido de reporte de programa de cargas")
        public void validateReportHasContent() throws Exception {
            LoadScheduleRequest request = new LoadScheduleRequest("PE1", "15/03/25", "0", "15:37");

            mockMvc.perform(post("/programa-cargas/validar").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Validación realizada correctamente"));

            verify(service).ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.status());
        }

        @Test
        @DisplayName("Generar reporte PDF de programa de carga")
        public void generateReport() throws Exception {
            LoadScheduleRequest request = new LoadScheduleRequest("PE1", "20/03/25", "0", "16:36");

            mockMvc.perform(post("/programa-cargas").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                    .andExpect(status().isOk());

            verify(service).generateReport(request);
        }
    }

    @Nested
    @DisplayName("Pruebas de casos de errores para generar reportes de programa de carga")
    public class GenerateReportErrorTest {
        @Test
        @DisplayName("Validar que reporte tenga contenido - error")
        public void generateReportWithErrors() throws Exception {
            LoadScheduleRequest request = new LoadScheduleRequest("PE1", "15/03/25", "0", "15:37");

            doThrow(new CargoDoesNotExistException(ErrorMessage.NO_CARGOS_FOUND.value()))
                    .when(service).ensureAnyCargoExists(any(), any(), any());

            mockMvc.perform(post("/programa-cargas/validar").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(ErrorMessage.NO_CARGOS_FOUND.value()));

            verify(service).ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.status());
        }
    }
}