package com.udb.aerovia_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "UpdateReservaItemDto", description = "Detalle de cambio sobre un asiento reservado")
public record UpdateReservaItemDto(
        @Schema(description = "Identificador del asiento reservado a modificar", example = "15", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "El identificador del asiento reservado es obligatorio.")
        Long reservaAsientoId,

        @Schema(description = "Nuevo pasajero asociado al asiento (opcional)", example = "8", nullable = true)
        Long pasajeroId,

        @Schema(description = "Nuevo asiento dentro del avion (opcional)", example = "102", nullable = true)
        Long asientoAvionId,

        @Schema(description = "Nueva tarifa a aplicar al asiento (opcional)", example = "4", nullable = true)
        Long tarifaOperacionId
) {
}

