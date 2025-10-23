package com.udb.aerovia_api.dto;

public record AvionDto(
    Long id,
    String matricula,
    String modelo,
    Integer capacidadTotal
) {}