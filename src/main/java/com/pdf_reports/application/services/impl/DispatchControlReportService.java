package com.pdf_reports.application.services.impl;

import com.pdf_reports.application.services.IDispatchControlReportService;
import com.pdf_reports.domain.models.dto.request.ReportRequest;
import com.pdf_reports.domain.repositories.IDispatchControlRepository;
import com.pdf_reports.utils.constants.values.DefaultValue;
import com.pdf_reports.utils.exceptions.CargoDoesNotExistException;
import com.pdf_reports.utils.constants.messages.ErrorMessage;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class DispatchControlReportService implements IDispatchControlReportService {
    private static final Logger logger = LoggerFactory.getLogger(DispatchControlReportService.class);
    private final IDispatchControlRepository repository;
    private final DataSource dataSource;

    public DispatchControlReportService(IDispatchControlRepository repository, DataSource dataSource) {
        this.repository = repository;
        this.dataSource = dataSource;
    }

    @Override
    public byte[] generateDispatchControlReport(ReportRequest request) {
        Connection cn = null;
        try {
            cn = dataSource.getConnection();
            InputStream reportStream = new ByteArrayInputStream(getResourceAsByteArray("/reports/dispatchOrderControl.jasper"));
            InputStream banner = new ByteArrayInputStream(getResourceAsByteArray("/reports/yobelbanner.png"));
            InputStream detailReport = new ByteArrayInputStream(getResourceAsByteArray("/reports/test.jasper"));

            Map<String, Object> params = new HashMap<>();
            params.put("banner", banner);
            params.put("details", detailReport);
            params.put("cd", request.cd());
            params.put("cargoNumber", request.cargoNumber());
            params.put("dispatchDate", request.dispatchDate());
            JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, params, cn);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if(cn != null) {
                try {
                    cn.close();
                } catch (SQLException e) {
                    logger.error(e.toString());
                    logger.error(e.getMessage());
                    logger.error(e.getLocalizedMessage());
                }
            }
        }
    }

    private InputStream getResource(String path) {
        return this.getClass().getResourceAsStream(path);
    }

    private byte[] getResourceAsByteArray(String path) throws IOException {
        try (InputStream is = getResource(path)) {
            if (is == null) {
                throw new IOException("Archivo no encontrado: " + path);
            }

            return is.readAllBytes();
        }
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