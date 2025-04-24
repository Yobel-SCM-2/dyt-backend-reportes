package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.ISupervisorService;
import com.pdf_reports.domain.models.dto.request.SupervisorRequest;
import com.pdf_reports.domain.models.dto.response.ReportHeader;
import com.pdf_reports.domain.models.dto.response.ReportParameters;
import com.pdf_reports.domain.models.dto.response.ReportSummary;
import com.pdf_reports.domain.repositories.ISupervisorRepository;
import com.pdf_reports.utils.constants.messages.ErrorMessage;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
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
public class SupervisorService implements ISupervisorService {
    private final ISupervisorRepository repository;
    private final DataSource dataSource;

    @Override
    public byte[] generateSupervisorReport(SupervisorRequest request) {
        ReportParameters parameters = repository.getReportParameters(request.account());
        ReportHeader header = repository.getReportHeader(request.account(), request.processDate().toLocalDateTime(), request.processBatch());
        ReportSummary summary = repository.getReportSummary(header.cd(), request.account(), request.processDate().toLocalDateTime(), request.processBatch());
        try(Connection cn = dataSource.getConnection()) {
            InputStream reportStream = getResource("/reports/loadScheduleSupervisor/SupervisorReport.jasper");
            JasperReport subReport = (JasperReport) JRLoader.loadObject(getResource("/reports/loadScheduleSupervisor/supervisorDetail.jasper"));
            InputStream banner = getResource("/reports/yobelbanner.png");
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, setParameters(subReport, banner, request.time(), parameters, header, request.processBatch(), Date.valueOf(request.processDate().toLocalDate()), summary), cn);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

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

    public Map<String, Object> setParameters(JasperReport details, InputStream banner, String time, ReportParameters parameters, ReportHeader header, int batch, Date date, ReportSummary summary) {
        Map<String, Object> params = new HashMap<>();
        params.put("agp", summary.totalAgp());
        params.put("totalOrder", summary.totalOrder());
        params.put("banner", banner);
        params.put("time", time);
        params.put("account", parameters.account());
        params.put("batch", batch);
        params.put("date", date);
        params.put("cd", header.cd());
        params.put("campaign", header.campaign());
        params.put("dispatchDate", header.dispatchDate());
        params.put("origin", parameters.cdDesc());
        params.put("detail", details);
        params.put("normalOrders", summary.normalOrders());
        return params;
    }
}