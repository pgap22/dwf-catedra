package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateClaseDto(
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 50, message = "El nombre no debe exceder los 50 caracteres.")
    String nombre,

    @Size(max = 200, message = "La descripción no debe exceder los 200 caracteres.")
    String descripcion
) {}