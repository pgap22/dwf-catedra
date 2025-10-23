package com.udb.aerovia_api.dto;

public record TarifaDto(
    Long id,
    String codigo,
    ClaseDto clase, // Incluimos el DTO de Clase
    boolean reembolsable
) {}