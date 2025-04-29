package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.IGeneralCargoService;
import com.pdf_reports.domain.models.dto.request.GeneralCargoRequest;
import com.pdf_reports.domain.models.dto.response.ReportHeader;
import com.pdf_reports.domain.models.dto.response.ReportParameters;
import com.pdf_reports.domain.repositories.IGeneralCargoRepository;
import com.pdf_reports.domain.repositories.ISupervisorRepository;
import com.pdf_reports.utils.constants.messages.ErrorMessage;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeneralCargoService implements IGeneralCargoService {
    private final IGeneralCargoRepository generalCargoRepository;
    private final ISupervisorRepository supervisorRepository;
    private final DataSource dataSource;

    @Override
    public byte[] generateGeneralCargoReport(GeneralCargoRequest request) {
        ReportParameters parameters = supervisorRepository.getReportParameters(request.account());
        ReportHeader header = supervisorRepository.getReportHeader(request.account(), request.processDate().toLocalDateTime(), request.processBatch());
        try(Connection cn = dataSource.getConnection()) {
            InputStream reportStream = getResource("/reports/generalCargo/generalCargo.jasper");
            InputStream banner = getResource("/reports/yobelbanner.png");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, setParameters(banner, request.time(), parameters, header, request.processBatch(), Date.valueOf(request.processDate().toLocalDate())), cn);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void ensureAnyCargoExists(GeneralCargoRequest request) {
        ZoneId zoneId = ZoneId.of(request.timeZone());
        ZonedDateTime processDate = request.processDate().atZoneSameInstant(zoneId);

        int cargoQuantity = generalCargoRepository.validate(request.account(), processDate.toLocalDateTime(), request.processBatch(), request.region(), request.lpr());

        if(cargoQuantity == 0) {
            throw new CargoDoesNotExistException(ErrorMessage.NO_ORDERS_FOUND.value());
        }
    }

    private InputStream getResource(String path) {
        return this.getClass().getResourceAsStream(path);
    }

    public Map<String, Object> setParameters(InputStream banner, String time, ReportParameters parameters, ReportHeader header, int batch, Date date) {
        Map<String, Object> params = new HashMap<>();
        params.put("banner", banner);
        params.put("time", time);
        params.put("account", parameters.account());
        params.put("accountDesc", parameters.accountDesc());
        params.put("batch", batch);
        params.put("processDate", date);
        params.put("cd", header.cd());
        params.put("campaign", header.campaign());
        params.put("dispatchDate", header.dispatchDate());
        params.put("cdDesc", parameters.cdDesc());
        return params;
    }
}