package com.udb.aerovia_api.dto;

public record AsientoAvionDto(
    Long id,
    Long avionId, // Solo ID para simplificar
    String codigoAsiento,
    ClaseDto clase // DTO de Clase
) {}