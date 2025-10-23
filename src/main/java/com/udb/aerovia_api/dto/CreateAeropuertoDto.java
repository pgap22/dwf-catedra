package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAeropuertoDto(
    @NotBlank(message = "El código IATA no puede estar vacío.")
    @Pattern(regexp = "^[A-Z]{3}$", message = "El código IATA debe consistir en 3 letras mayúsculas.")
    String codigoIata,

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 150, message = "El nombre no debe exceder los 150 caracteres.")
    String nombre,

    @NotBlank(message = "La ciudad no puede estar vacía.")
    @Size(max = 120, message = "La ciudad no debe exceder los 120 caracteres.")
    String ciudad,

    @NotBlank(message = "El país no puede estar vacío.")
    @Size(max = 120, message = "El país no debe exceder los 120 caracteres.")
    String pais
) {}