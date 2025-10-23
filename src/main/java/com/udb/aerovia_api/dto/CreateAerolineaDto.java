package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAerolineaDto(
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 150, message = "El nombre no debe exceder los 150 caracteres.")
    String nombre,

    @NotBlank(message = "El código IATA no puede estar vacío.")
    @Pattern(regexp = "^[A-Z0-9]{2,3}$", message = "El código IATA debe consistir en 2 o 3 letras mayúsculas o números.")
    String codigoIata
) {}