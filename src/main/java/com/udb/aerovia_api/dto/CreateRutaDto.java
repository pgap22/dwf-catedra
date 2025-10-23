package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateRutaDto(
    @NotNull(message = "El ID del aeropuerto de origen es obligatorio.")
    Long origenId,

    @NotNull(message = "El ID del aeropuerto de destino es obligatorio.")
    Long destinoId,

    @NotNull(message = "La distancia en kilometros es obligatoria.")
    @Min(value = 1, message = "La distancia debe ser mayor a 0.")
    Integer distanciaKm
) {}

