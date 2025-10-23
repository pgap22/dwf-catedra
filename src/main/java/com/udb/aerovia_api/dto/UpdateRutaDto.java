package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.Min;

public record UpdateRutaDto(
        Long origenId,
        Long destinoId,
        @Min(value = 1, message = "La distancia debe ser mayor a 0.")
        Integer distanciaKm
) {
}
