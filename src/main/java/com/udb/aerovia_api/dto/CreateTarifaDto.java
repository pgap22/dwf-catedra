package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateTarifaDto(
    @NotBlank(message = "El código de la tarifa no puede estar vacío.")
    @Size(max = 20, message = "El código no debe exceder los 20 caracteres.")
    String codigo,

    @NotNull(message = "El ID de la clase es obligatorio.")
    Long claseId,

    @NotNull(message = "Debe indicar si la tarifa es reembolsable.")
    Boolean reembolsable
) {}