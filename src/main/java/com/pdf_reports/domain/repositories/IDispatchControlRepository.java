package com.pdf_reports.domain.repositories;

public interface IDispatchControlRepository {
    int quantityByCargoNumber(String cd, String dispatchDate, int cargoNumber);
    int quantityByFilters(String cd, String dispatchDate);
}