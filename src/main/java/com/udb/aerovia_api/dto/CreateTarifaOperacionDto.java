package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateTarifaOperacionDto(
    @NotNull(message = "El ID de la operación de vuelo es obligatorio.")
    Long operacionId,

    @NotNull(message = "El ID de la tarifa es obligatorio.")
    Long tarifaId,

    @NotNull(message = "El precio es obligatorio.")
    @PositiveOrZero(message = "El precio no puede ser negativo.")
    @Digits(integer=10, fraction=2, message = "El precio debe tener máximo 10 dígitos enteros y 2 decimales.")
    BigDecimal precio,

    @NotNull(message = "La cantidad de asientos disponibles es obligatoria.")
    @Min(value = 0, message = "La cantidad de asientos no puede ser negativa.")
    Integer asientosDisponibles
) {}