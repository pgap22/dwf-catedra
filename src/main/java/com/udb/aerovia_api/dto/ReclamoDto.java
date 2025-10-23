package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.EstadoReclamo;

public record ReclamoDto(
    Long id,
    Long reservaId,
    PasajeroDto pasajero,
    String descripcion,
    EstadoReclamo estado
) {}