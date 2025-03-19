package com.pdf_reports.infraestructure.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdf_reports.application.services.IDispatchControlService;
import com.pdf_reports.domain.models.dto.request.DispatchControlRequest;
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

@WebMvcTest(DispatchReportController.class)
public class DispatchReportControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private IDispatchControlService service;

    @Nested
    @DisplayName("Pruebas de casos exitosos para generar reporte de control de despacho")
    public class SuccessfullyDispatchControlReport {
        @Test
        @DisplayName("Prueba validar que haya una carga para la fecha de despacho y CD")
        public void testValidateContent() throws Exception {
            DispatchControlRequest request = new DispatchControlRequest("PE1", "20/03/25", 0, "17:16");

            mockMvc.perform(post("/control-despacho/validar").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Validación realizada correctamente"));

            verify(service).ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.cargoNumber());
        }

        @Test
        @DisplayName("Prueba validar que haya una carga para la fecha de despacho, CD y número de carga ingresado")
        public void testValidateContentByCargoNumber() throws Exception {
            DispatchControlRequest request = new DispatchControlRequest("PE1", "20/03/25", 35, "17:16");

            mockMvc.perform(post("/control-despacho/validar").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Validación realizada correctamente"));

            verify(service).ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.cargoNumber());
        }

        @Test
        @DisplayName("Prueba generar reporte PDF de control de despacho")
        public void testGenerateReport() throws Exception {
            DispatchControlRequest request = new DispatchControlRequest("PE1", "20/03/25", 0, "17:30");

            mockMvc.perform(post("/control-despacho").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_PDF));

            verify(service).generateDispatchControlReport(request);
        }
    }

    @Nested
    @DisplayName("Pruebas de casos de error para generar reporte de control de despacho")
    public class ErrorDispatchControlReport {
        @Test
        @DisplayName("Prueba error al validar que haya al menos una carga para el CD y fecha de despacho")
        public void testValidateContentFailed() throws Exception {
            DispatchControlRequest request = new DispatchControlRequest("PE1", "01/03/25", 0, "17:37");

            doThrow(new CargoDoesNotExistException(ErrorMessage.NO_CARGOS_FOUND.value()))
                    .when(service).ensureAnyCargoExists(any(), any(), anyInt());

            mockMvc.perform(post("/control-despacho/validar").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(ErrorMessage.NO_CARGOS_FOUND.value()));

            verify(service).ensureAnyCargoExists(any(), any(), anyInt());
        }

        @Test
        @DisplayName("Prueba error al validar que haya al menos una carga para el CD, fecha de despacho y número de carga")
        public void testValidateContentByCargoNumberFailed() throws Exception {
            DispatchControlRequest request = new DispatchControlRequest("PE1", "20/03/25", 1, "17:44");

            doThrow(new CargoDoesNotExistException(ErrorMessage.NO_CARGOS_FOUND.value()))
                    .when(service).ensureAnyCargoExists(any(), any(), anyInt());

            mockMvc.perform(post("/control-despacho/validar").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(ErrorMessage.NO_CARGOS_FOUND.value()));

            verify(service).ensureAnyCargoExists(request.cd(), request.dispatchDate(), request.cargoNumber());
        }
    }
}