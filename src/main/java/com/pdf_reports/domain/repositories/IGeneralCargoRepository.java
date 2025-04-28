package com.pdf_reports.domain.repositories;

import java.time.LocalDateTime;

public interface IGeneralCargoRepository {
    int validate(String account, LocalDateTime processDate, int processBatch, int region, String lpr);
}