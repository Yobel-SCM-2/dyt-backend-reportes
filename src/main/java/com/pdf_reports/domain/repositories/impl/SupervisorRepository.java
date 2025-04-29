package com.pdf_reports.domain.repositories.impl;

import com.pdf_reports.domain.models.dto.response.ReportHeader;
import com.pdf_reports.domain.models.dto.response.ReportParameters;
import com.pdf_reports.domain.repositories.ISupervisorRepository;
import com.pdf_reports.utils.configs.ReportHeaderRowMapper;
import com.pdf_reports.utils.configs.ReportParameterRowMapper;
import lombok.RequiredArgsConstructor;
import org.hibernate.dialect.OracleTypes;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SupervisorRepository implements ISupervisorRepository {
    private final DataSource dataSource;

    @Override
    public int validate(String account, LocalDateTime processDate, int processBatch) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("PKG_DYT_REPORTES_CONTROL_DESPACHO")
                .withProcedureName("CARGAS_SUPERVISOR_VALIDACION")
                .declareParameters(
                        new SqlParameter("p_account", Types.VARCHAR),
                        new SqlParameter("p_process_date", Types.DATE),
                        new SqlParameter("p_batch", Types.VARCHAR),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Date timestamp = Date.valueOf(processDate.toLocalDate());

        Map<String, Object> result = jdbcCall.execute(Map.of(
                        "p_account", account,
                        "p_process_date", timestamp,
                        "p_batch", processBatch
                ));

        return (int) result.get("p_result");
    }

    @Override
    public ReportParameters getReportParameters(String account) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("PKG_DYT_REPORTES_CONTROL_DESPACHO")
                .withProcedureName("REPORTE_SUPERVISOR_PARAMETROS")
                .declareParameters(
                        new SqlParameter("p_account", Types.VARCHAR),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, new ReportParameterRowMapper())
                );

        Map<String, Object> result = jdbcCall.execute(Map.of("p_account", account));
        List<ReportParameters> parameters = (List<ReportParameters>) result.get("p_result");

        return parameters.getFirst();
    }

    @Override
    public ReportHeader getReportHeader(String account, LocalDateTime processDate, int processBatch) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("PKG_DYT_REPORTES_CONTROL_DESPACHO")
                .withProcedureName("REPORTE_SUPERVISOR_HEADER")
                .declareParameters(
                        new SqlParameter("p_account", Types.VARCHAR),
                        new SqlParameter("p_process_date", Types.DATE),
                        new SqlParameter("p_batch", Types.VARCHAR),
                        new SqlOutParameter("p_result", OracleTypes.CURSOR, new ReportHeaderRowMapper())
                );

        Date timestamp = Date.valueOf(processDate.toLocalDate());

        Map<String, Object> result = jdbcCall.execute(Map.of(
                "p_account", account,
                "p_process_date", timestamp,
                "p_batch", processBatch
        ));

        List<ReportHeader> header = (List<ReportHeader>) result.get("p_result");

        return header.getFirst();
    }
}