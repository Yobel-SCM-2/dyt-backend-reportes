package com.pdf_reports.utils.configs;

import com.pdf_reports.domain.models.dto.response.ReportSummary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportSummaryRowMapper implements RowMapper<ReportSummary> {
    @Override
    public ReportSummary mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new ReportSummary(
                rs.getInt("totalOrder"),
                rs.getInt("totalAgp"),
                rs.getInt("normalOrders")
        );
    }
}