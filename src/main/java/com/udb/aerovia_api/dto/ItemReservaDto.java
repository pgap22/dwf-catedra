package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotNull;

public record ItemReservaDto(
    @NotNull(message = "El ID del pasajero es obligatorio.")
    Long pasajeroId,

    @NotNull(message = "El ID del asiento del avión es obligatorio.")
    Long asientoAvionId,

    @NotNull(message = "El ID de la tarifa de operación (precio) es obligatorio.")
    Long tarifaOperacionId
) {}