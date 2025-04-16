package com.pdf_reports.domain.repositories;

import java.time.LocalDateTime;

public interface ISupervisorRepository {
    int validate(String account, LocalDateTime processDate, int processBatch);
}