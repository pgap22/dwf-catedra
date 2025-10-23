package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.EstadoReserva;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservaAdminDto(
        Long id,
        String codigoReserva,
        EstadoReserva estado,
        LocalDateTime fechaReserva,
        BigDecimal total,
        String clienteNombre,
        String clienteCorreo,
        String aerolinea,
        String origen,
        String destino
) {
}
