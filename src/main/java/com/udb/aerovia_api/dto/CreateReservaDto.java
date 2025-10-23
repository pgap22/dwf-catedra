package com.udb.aerovia_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateReservaDto(
    // El ID del vuelo específico que se está reservando
    @NotNull(message = "El ID de la operación de vuelo es obligatorio.")
    Long operacionVueloId,

    // Lista de pasajeros y sus asientos/tarifas asignados
    @NotEmpty(message = "Debe haber al menos un pasajero en la reserva.")
    List<@Valid ItemReservaDto> items // @Valid aquí valida cada item de la lista
) {}