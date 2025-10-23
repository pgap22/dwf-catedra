package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.EstadoReserva;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Incluye detalles del vuelo, pasajeros y asientos
public record ReservaDetalleDto(
    Long id,
    String codigoReserva,
    UserDto usuario, // Quien hizo la reserva
    LocalDateTime fechaReserva,
    EstadoReserva estado,
    BigDecimal total,
    OperacionVueloDto operacionVuelo, // Detalles del vuelo
    List<ReservaAsientoDetalleDto> asientos // Detalles de cada asiento/pasajero
) {}
