package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.ISupervisorService;
import com.pdf_reports.domain.models.dto.request.SupervisorRequest;
import com.pdf_reports.domain.repositories.ISupervisorRepository;
import com.pdf_reports.utils.constants.messages.ErrorMessage;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SupervisorService implements ISupervisorService {
    private final ISupervisorRepository repository;
    private final DataSource dataSource;

    @Override
    public byte[] generateSupervisorReport(SupervisorRequest request) {
        try(Connection cn = dataSource.getConnection()) {
            InputStream reportStream = getResource("/reports/loadScheduleSupervisor/loadSchedule.jasper");
            InputStream banner = getResource("/reports/yobelbanner.png");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, setParameters(banner), cn);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }    }

    @Override
    public void ensureAnyCargoExists(SupervisorRequest request) {
        ZoneId zoneId = ZoneId.of(request.timeZone());
        ZonedDateTime processDate = request.processDate().atZoneSameInstant(zoneId);

        int cargoQuantity = repository.validate(request.account(), processDate.toLocalDateTime(), request.processBatch());

        if(cargoQuantity == 0) {
            throw new CargoDoesNotExistException(ErrorMessage.NO_ORDERS_FOUND.value());
        }
    }

    private InputStream getResource(String path) {
        return this.getClass().getResourceAsStream(path);
    }

    public Map<String, Object> setParameters(InputStream banner) {
        Map<String, Object> params = new HashMap<>();

        return params;
    }
}