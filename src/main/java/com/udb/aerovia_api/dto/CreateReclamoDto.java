package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReclamoDto(
    @NotNull(message = "El ID de la reserva es obligatorio.")
    Long reservaId,

    @NotNull(message = "El ID del pasajero que reclama es obligatorio.")
    Long pasajeroId,

    @NotBlank(message = "La descripción no puede estar vacía.")
    @Size(max = 500, message = "La descripción no debe exceder los 500 caracteres.")
    String descripcion
) {}