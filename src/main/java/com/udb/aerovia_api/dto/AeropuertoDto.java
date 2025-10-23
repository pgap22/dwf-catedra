package com.udb.aerovia_api.dto;

public record AeropuertoDto(
    Long id,
    String codigoIata,
    String nombre,
    String ciudad,
    String pais
) {}