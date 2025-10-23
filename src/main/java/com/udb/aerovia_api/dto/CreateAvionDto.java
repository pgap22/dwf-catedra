package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateAvionDto(
    @NotBlank(message = "La matrícula no puede estar vacía.")
    @Size(max = 20, message = "La matrícula no debe exceder los 20 caracteres.")
    String matricula,

    @NotBlank(message = "El modelo no puede estar vacío.")
    @Size(max = 80, message = "El modelo no debe exceder los 80 caracteres.")
    String modelo,

    @NotNull(message = "La capacidad total es obligatoria.")
    @Min(value = 1, message = "La capacidad total debe ser al menos 1.")
    Integer capacidadTotal
) {}