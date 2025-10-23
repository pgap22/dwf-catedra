package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreatePagoDto(
    @NotNull(message = "El ID de la reserva es obligatorio.")
    Long reservaId,

    @NotNull(message = "La fecha de pago es obligatoria.")
    LocalDateTime fechaPago,

    @NotBlank(message = "El método de pago no puede estar vacío.")
    @Size(max = 40)
    String metodoPago,

    @NotNull(message = "El monto es obligatorio.")
    @Positive(message = "El monto debe ser positivo.")
    @Digits(integer=10, fraction=2, message = "El monto debe tener máximo 10 dígitos enteros y 2 decimales.")
    BigDecimal monto
) {}