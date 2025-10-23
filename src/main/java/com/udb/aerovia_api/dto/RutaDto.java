package com.udb.aerovia_api.dto;

public record RutaDto(
    Long id,
    AeropuertoDto origen,
    AeropuertoDto destino,
    Integer distanciaKm
) {}