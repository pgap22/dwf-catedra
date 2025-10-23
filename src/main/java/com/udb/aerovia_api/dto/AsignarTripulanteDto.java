package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AsignarTripulanteDto(
    @NotNull(message = "El ID de la operación de vuelo es obligatorio.")
    Long operacionId,

    @NotNull(message = "El ID del tripulante es obligatorio.")
    Long tripulanteId,

    @NotBlank(message = "El rol en el vuelo no puede estar vacío.")
    @Size(max = 50)
    String rolEnVuelo
) {}