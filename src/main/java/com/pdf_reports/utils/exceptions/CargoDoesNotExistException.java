package com.pdf_reports.utils.exceptions;

public class CargoDoesNotExistException extends RuntimeException {
    public CargoDoesNotExistException(String message) {
        super(message);
    }
}