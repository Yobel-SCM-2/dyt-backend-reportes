package com.pdf_reports.utils.exceptions;

public class CargoStatusDoesNotExist extends RuntimeException {
    public CargoStatusDoesNotExist(String message) {
        super(message);
    }
}