package com.udb.aerovia_api.dto;

import java.math.BigDecimal;

public record TarifaOperacionDto(
    Long id,
    Long operacionId,
    TarifaDto tarifa, // Incluimos el DTO de Tarifa
    BigDecimal precio,
    Integer asientosDisponibles
) {}