package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.EstadoOperacionVuelo;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateOperacionVueloDto(
    @NotNull(message = "El ID del vuelo es obligatorio.")
    Long vueloId,

    @NotNull(message = "El ID del avión es obligatorio.")
    Long avionId,

    @NotNull(message = "La fecha de salida es obligatoria.")
    @Future(message = "La fecha de salida debe ser en el futuro.")
    LocalDateTime fechaSalida,

    @NotNull(message = "La fecha de llegada es obligatoria.")
    @Future(message = "La fecha de llegada debe ser en el futuro.")
    LocalDateTime fechaLlegada,

    @NotNull(message = "El estado es obligatorio.")
    EstadoOperacionVuelo estado
) {}