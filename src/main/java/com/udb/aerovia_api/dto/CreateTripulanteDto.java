package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTripulanteDto(
    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(max = 150, message = "El nombre no debe exceder los 150 caracteres.")
    String nombre,

    @NotBlank(message = "El tipo no puede estar vacío.")
    @Size(max = 50, message = "El tipo no debe exceder los 50 caracteres.")
    String tipo // Ej: PILOTO, COPILOTO, TCP
) {}