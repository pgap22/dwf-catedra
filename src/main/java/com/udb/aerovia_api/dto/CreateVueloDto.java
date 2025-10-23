package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateVueloDto(
    @NotBlank(message = "El número de vuelo no puede estar vacío.")
    @Pattern(regexp = "^[A-Z0-9]{2}\\d{1,4}$", message = "El número de vuelo debe tener un formato como AA1234.")
    String numeroVuelo,

    @NotNull(message = "El ID de la aerolínea es obligatorio.")
    Long aerolineaId,

    @NotNull(message = "El ID de la ruta es obligatorio.")
    Long rutaId,

    @NotNull(message = "La duración en minutos es obligatoria.")
    @Min(value = 1, message = "La duración debe ser mayor a 0.")
    Integer duracionMin
) {}