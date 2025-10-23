package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.EstadoOperacionVuelo;
import java.time.LocalDateTime;

public record OperacionVueloDto(
    Long id,
    VueloDto vuelo,
    AvionDto avion,
    LocalDateTime fechaSalida,
    LocalDateTime fechaLlegada,
    EstadoOperacionVuelo estado
) {}