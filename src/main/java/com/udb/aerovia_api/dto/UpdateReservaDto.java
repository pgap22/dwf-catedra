package com.udb.aerovia_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Schema(name = "UpdateReservaDto", description = "Solicitud PATCH para modificar asientos, pasajeros o tarifas de una reserva")
public record UpdateReservaDto(
        @Schema(description = "Coleccion de cambios a aplicar sobre la reserva", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "Debe enviar al menos un cambio")
        List<@Valid UpdateReservaItemDto> cambios
) {
}
