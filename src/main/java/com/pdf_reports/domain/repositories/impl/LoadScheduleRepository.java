package com.pdf_reports.domain.repositories.impl;

import com.pdf_reports.domain.repositories.ILoadScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@RequiredArgsConstructor
@Repository
public class LoadScheduleRepository implements ILoadScheduleRepository {
    private final DataSource dataSource;

    @Override
    public int validate(String cd, String dispatchDate, String status) {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(dataSource)
                .withCatalogName("PKG_DYT_REPORTES_CONTROL_DESPACHO")
                .withProcedureName("PROGRAMA_CARGAS_VALIDACION")
                .declareParameters(
                        new SqlParameter("p_cd", Types.VARCHAR),
                        new SqlParameter("p_cd", Types.VARCHAR),
                        new SqlParameter("p_dispatch_date", Types.VARCHAR),
                        new SqlParameter("p_status_cargo", Types.VARCHAR),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Map<String, Object> result = jdbcCall.execute(
                new SqlParameterValue(Types.VARCHAR, cd),
                new SqlParameterValue(Types.VARCHAR, dispatchDate),
                new SqlParameterValue(Types.VARCHAR, status)
        );

        return (int) result.get("p_result");
    }
}