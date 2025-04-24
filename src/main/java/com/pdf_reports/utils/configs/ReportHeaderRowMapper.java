package com.pdf_reports.utils.configs;

import com.pdf_reports.domain.models.dto.response.ReportHeader;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportHeaderRowMapper implements RowMapper<ReportHeader> {
    @Override
    public ReportHeader mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReportHeader(
                rs.getString("cd"),
                rs.getString("campaign"),
                rs.getDate("dispatchDate")
        );
    }
}