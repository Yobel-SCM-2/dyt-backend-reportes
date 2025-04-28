package com.pdf_reports.domain.repositories.impl;

import com.pdf_reports.domain.repositories.IGeneralCargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GeneralCargoRepository implements IGeneralCargoRepository {
    private final DataSource dataSource;

    @Override
    public int validate(String account, LocalDateTime processDate, int processBatch, int region, String lpr) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("PKG_DYT_REPORTES_CONTROL_DESPACHO")
                .withProcedureName("CARGAS_GENERAL_VALIDACION")
                .declareParameters(
                        new SqlParameter("p_account", Types.VARCHAR),
                        new SqlParameter("p_process_date", Types.DATE),
                        new SqlParameter("p_batch", Types.VARCHAR),
                        new SqlParameter("p_region", Types.INTEGER),
                        new SqlParameter("p_lprs", Types.VARCHAR),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Date timestamp = Date.valueOf(processDate.toLocalDate());

        Map<String, Object> result = jdbcCall.execute(Map.of(
                "p_account", account,
                "p_process_date", timestamp,
                "p_batch", processBatch,
                "p_region", region,
                "p_lprs", lpr
        ));

        return (int) result.get("p_result");
    }
}