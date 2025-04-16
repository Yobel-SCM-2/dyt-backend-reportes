package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.ISupervisorService;
import com.pdf_reports.domain.models.dto.request.SupervisorRequest;
import com.pdf_reports.domain.repositories.ISupervisorRepository;
import com.pdf_reports.utils.constants.messages.ErrorMessage;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class SupervisorService implements ISupervisorService {
    private final ISupervisorRepository repository;

    @Override
    public byte[] generateSupervisorReport(SupervisorRequest request) {
        return new byte[0];
    }

    @Override
    public void ensureAnyCargoExists(SupervisorRequest request) {
        ZoneId zoneId = ZoneId.of(request.timeZone());
        ZonedDateTime processDate = request.processDate().atZoneSameInstant(zoneId);

        int cargoQuantity = repository.validate(request.account(), processDate.toLocalDateTime(), request.processBatch());

        if(cargoQuantity == 0) {
            throw new CargoDoesNotExistException(ErrorMessage.NO_CARGOS_FOUND.value());
        }
    }
}