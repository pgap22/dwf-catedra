package com.udb.aerovia_api.dto;

public record EstadisticasDto(
    long totalReservasActivas,
    long totalReservasCanceladas,
    long totalReservasCompletadas,
    long totalCancelaciones // Podría ser redundante si ya tenemos reservas canceladas
    // Podríamos añadir más estadísticas: total de pasajeros, total de vuelos programados, etc.
) {}