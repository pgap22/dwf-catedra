package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record BusquedaVueloParamsDto(
    @NotNull(message = "El ID del aeropuerto de origen es obligatorio.")
    Long origenId,

    @NotNull(message = "El ID del aeropuerto de destino es obligatorio.")
    Long destinoId,

    @NotNull(message = "La fecha de salida es obligatoria.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) // Asegura formato YYYY-MM-DD
    @FutureOrPresent(message = "La fecha de salida no puede ser en el pasado.")
    LocalDate fechaSalida
) {}