package com.udb.aerovia_api.dto;

import java.time.LocalDate;

public record PasajeroDto(
    Long id,
    String nombreCompleto,
    LocalDate fechaNacimiento,
    String nroPasaporte,
    Long usuarioId
) {}
