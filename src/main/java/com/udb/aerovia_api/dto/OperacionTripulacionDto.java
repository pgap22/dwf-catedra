package com.udb.aerovia_api.dto;

public record OperacionTripulacionDto(
    Long id,
    Long operacionId,
    TripulanteDto tripulante,
    String rolEnVuelo
) {}