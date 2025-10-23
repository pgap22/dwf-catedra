package com.udb.aerovia_api.dto;

public record VueloDto(
    Long id,
    String numeroVuelo,
    AerolineaDto aerolinea,
    RutaDto ruta,
    Integer duracionMin
) {}