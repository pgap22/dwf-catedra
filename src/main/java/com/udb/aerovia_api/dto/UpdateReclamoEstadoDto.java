package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.EstadoReclamo;
import jakarta.validation.constraints.NotNull;

public record UpdateReclamoEstadoDto(
    @NotNull(message = "El nuevo estado es obligatorio.")
    EstadoReclamo estado
) {}