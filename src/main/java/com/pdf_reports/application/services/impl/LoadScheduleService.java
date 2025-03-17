package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.ILoadScheduleService;
import com.pdf_reports.domain.models.dto.request.LoadScheduleRequest;
import com.pdf_reports.domain.repositories.ILoadScheduleRepository;
import com.pdf_reports.utils.constants.messages.ErrorMessage;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import com.pdf_reports.utils.exceptions.CargoStatusDoesNotExist;
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
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class LoadScheduleService implements ILoadScheduleService {
    private final ILoadScheduleRepository repository;
    private final DataSource dataSource;

    @Override
    public byte[] generateReport(LoadScheduleRequest request) {
        try(Connection cn = dataSource.getConnection()) {
            InputStream reportStream = getResource("/reports/loadSchedule/loadSchedule.jasper");
            InputStream banner = getResource("/reports/yobelbanner.png");
            JasperReport detail = (JasperReport) JRLoader.loadObject(getResource("/reports/loadSchedule/loadScheduleDetail.jasper"));
            JasperReport cargoDetails = (JasperReport) JRLoader.loadObject(getResource("/reports/loadSchedule/loadScheduleOrders.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, setParameters(request, detail, cargoDetails, banner), cn);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> setParameters(LoadScheduleRequest request, JasperReport detail, JasperReport cargoDetail, InputStream banner) {
        Map<String, Object> params = new HashMap<>();
        params.put("time", request.time());
        params.put("subtitle", getReportSubtitle(request.status()));
        params.put("CD", request.cd());
        params.put("cdDescription", "");
        params.put("dispatchDate", request.dispatchDate());
        params.put("stsCargo", request.status());
        params.put("reportDetails", detail);
        params.put("cargoDetails", cargoDetail);
        params.put("banner", banner);

        return params;
    }

    private String getReportSubtitle(String status) {
        Map<String, Object> statusMap = Map.of("0", "PENDIENTES", "2", "CONFIRMADAS");
        if(!statusMap.containsKey(status)) {
            throw new CargoStatusDoesNotExist(ErrorMessage.STATUS_DOES_NOT_EXIST.value());
        }

        return statusMap.get(status).toString();
    }

    private InputStream getResource(String path) {
        return this.getClass().getResourceAsStream(path);
    }

    @Override
    public void ensureAnyCargoExists(String cd, String dispatchDate, String status) {
        int cargoQuantity = repository.validate(cd, dispatchDate, status);

        if(cargoQuantity == 0) {
            throw new CargoDoesNotExistException(ErrorMessage.NO_CARGOS_FOUND.value());
        }
    }
}