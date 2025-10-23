package com.udb.aerovia_api.dto;

import java.math.BigDecimal;

public record ReservaAsientoDetalleDto(
    Long id,
    PasajeroDto pasajero,
    AsientoAvionDto asiento,
    TarifaDto tarifa,
    BigDecimal precioPagado
) {}