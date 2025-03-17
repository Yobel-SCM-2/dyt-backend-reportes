package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.IDispatchControlService;
import com.pdf_reports.domain.models.dto.request.DispatchControlRequest;
import com.pdf_reports.domain.repositories.IDispatchControlRepository;
import com.pdf_reports.utils.constants.values.DefaultValue;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import com.pdf_reports.utils.constants.messages.ErrorMessage;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@Service
public class DispatchControlService implements IDispatchControlService {
    private static final Logger logger = LoggerFactory.getLogger(DispatchControlService.class);
    private final IDispatchControlRepository repository;
    private final DataSource dataSource;

    public DispatchControlService(IDispatchControlRepository repository, DataSource dataSource) {
        this.repository = repository;
        this.dataSource = dataSource;
    }

    @Override
    public byte[] generateDispatchControlReport(DispatchControlRequest request) {
        try(Connection cn = dataSource.getConnection()) {
            InputStream reportStream = getResource("/reports/dispatchControl/dispatchControl.jasper");
            InputStream banner = getResource("/reports/yobelbanner.png");
            JasperReport detailReport = (JasperReport) JRLoader.loadObject(getResource("/reports/dispatchControl/test.jasper"));
            Map<String, Object> params = new HashMap<>();
            params.put("time", request.time());
            params.put("banner", banner);
            params.put("details", detailReport);
            params.put("cd", request.cd());
            params.put("cargoNumber", request.cargoNumber());
            params.put("dispatchDate", request.dispatchDate());
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, cn);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private InputStream getResource(String path) {
        return this.getClass().getResourceAsStream(path);
    }

    @Override
    public void ensureAnyCargoExists(String cd, String dispatchDate, int cargoNumber) {
        if(cargoNumber == DefaultValue.NO_CARGO_PARAMETER) {
            filtersHasAnyCargo(cd, dispatchDate);
        }else {
            existsCargo(cd, dispatchDate, cargoNumber);
        }
    }

    private void filtersHasAnyCargo(String cd, String dispatchDate) {
        int cargoQuantity = repository.quantityByFilters(cd, dispatchDate);

        if(cargoQuantity == 0) {
            throw new CargoDoesNotExistException(ErrorMessage.NO_CARGOS_FOUND.value());
        }
    }

    private void existsCargo(String cd, String dispatchDate, int cargoNumber) {
        int existCargo = repository.quantityByCargoNumber(cd, dispatchDate, cargoNumber);

        if(existCargo == 0) {
            throw new CargoDoesNotExistException(ErrorMessage.CARGO_DOES_NOT_EXIST.value());
        }
    }
}