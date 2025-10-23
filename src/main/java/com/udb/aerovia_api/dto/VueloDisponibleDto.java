package com.udb.aerovia_api.dto;

import java.time.LocalDateTime;
import java.util.List;

public record VueloDisponibleDto(
    Long operacionId,
    String numeroVuelo,
    AerolineaDto aerolinea,
    AeropuertoDto origen,
    AeropuertoDto destino,
    LocalDateTime fechaSalida,
    LocalDateTime fechaLlegada,
    Integer duracionMin,
    AvionDto avion,
    List<TarifaOperacionDto> tarifasDisponibles // Incluye precios y asientos por tarifa
) {}