package com.pdf_reports.domain.repositories;

public interface ILoadScheduleRepository {
    int validate(String cd, String dispatchDate, String status);
}