package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotNull;

public record ItemReservaDto(
    @NotNull(message = "El ID del pasajero es obligatorio.")
    Long pasajeroId,

    // Opcional: si es nulo el sistema asignara un asiento disponible acorde a la tarifa
    Long asientoAvionId,

    @NotNull(message = "El ID de la tarifa de operacion (precio) es obligatorio.")
    Long tarifaOperacionId
) {}
