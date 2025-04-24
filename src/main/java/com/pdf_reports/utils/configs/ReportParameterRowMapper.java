package com.pdf_reports.utils.configs;

import com.pdf_reports.domain.models.dto.response.ReportParameters;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportParameterRowMapper implements RowMapper<ReportParameters> {

    @Override
    public ReportParameters mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReportParameters(
                rs.getString("account"),
                rs.getString("accountDesc"),
                rs.getString("cd"),
                rs.getString("cdDesc")
        );
    }
}