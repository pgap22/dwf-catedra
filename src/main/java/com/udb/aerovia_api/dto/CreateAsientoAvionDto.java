package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAsientoAvionDto(
    @NotNull(message = "El ID del avión es obligatorio.")
    Long avionId,

    @NotBlank(message = "El código del asiento no puede estar vacío.")
    @Size(max = 10, message = "El código del asiento no debe exceder los 10 caracteres.")
    String codigoAsiento,

    @NotNull(message = "El ID de la clase es obligatorio.")
    Long claseId
) {}