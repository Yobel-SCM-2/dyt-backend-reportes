package com.pdf_reports.domain.repositories.impl;

import com.pdf_reports.domain.repositories.IDispatchControlRepository;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Map;

@Repository
public class DispatchControlRepository implements IDispatchControlRepository {
    private final DataSource dataSource;

    public DispatchControlRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int quantityByCargoNumber(String cd, String dispatchDate, int cargoNumber) {
        SimpleJdbcCall jdbcCall = getJdbcCall("VALIDATE_CARGO_EXISTENCE")
                .declareParameters(
                        new SqlParameter("p_cd", Types.VARCHAR),
                        new SqlParameter("p_dispatch_date", Types.VARCHAR),
                        new SqlParameter("p_cargo", Types.INTEGER),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Map<String, Object> queryResult = jdbcCall.execute(
                new SqlParameterValue(Types.VARCHAR, cd),
                new SqlParameterValue(Types.VARCHAR, dispatchDate),
                new SqlParameterValue(Types.INTEGER, cargoNumber)
        );

        return (int) queryResult.get("p_result");
    }

    @Override
    public int quantityByFilters(String cd, String dispatchDate) {
        SimpleJdbcCall jdbcCall = getJdbcCall("VALIDATE_CARGO_COUNT_BY_FILTERS")
                .declareParameters(
                        new SqlParameter("p_cd", Types.VARCHAR),
                        new SqlParameter("p_dispatch_date", Types.VARCHAR),
                        new SqlOutParameter("p_result", Types.INTEGER)
                );

        Map<String, Object> queryResult = jdbcCall.execute(
                new SqlParameterValue(Types.VARCHAR, cd),
                new SqlParameterValue(Types.VARCHAR, dispatchDate)
        );

        return (int) queryResult.get("p_result");
    }

    private SimpleJdbcCall getJdbcCall(String procedure) {
        return new SimpleJdbcCall(dataSource)
                .withCatalogName("PKG_DYT_REPORTES_CONTROL_DESPACHO")
                .withProcedureName(procedure);
    }
}