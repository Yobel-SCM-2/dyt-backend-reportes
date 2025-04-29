package com.pdf_reports.utils.constants.messages;

public enum ErrorMessage {
    CARGO_DOES_NOT_EXIST("Carga no existe para el CD y fecha ingresados"),
    NO_CARGOS_FOUND("No existen cargas para el CD y fecha ingresados"),
    STATUS_DOES_NOT_EXIST("El status enviado no es válido"),
    NO_ORDERS_FOUND("No existen ordenes para los parámetros ingresados");
    private final String description;

    ErrorMessage(String description) {
        this.description = description;
    }

    public String value() {
        return this.description;
    }
}