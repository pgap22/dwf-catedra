package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotNull;

public record CreateCancelacionDto(
    @NotNull(message = "El ID de la reserva a cancelar es obligatorio.")
    Long reservaId
) {}