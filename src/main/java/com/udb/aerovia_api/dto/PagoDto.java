package com.udb.aerovia_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagoDto(
    Long id,
    Long reservaId, // Solo el ID en el DTO para simplificar
    LocalDateTime fechaPago,
    String metodoPago,
    BigDecimal monto
) {}