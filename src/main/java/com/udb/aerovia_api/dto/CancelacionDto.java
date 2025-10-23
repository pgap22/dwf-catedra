package com.udb.aerovia_api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CancelacionDto(
    Long id,
    Long reservaId,
    LocalDateTime fechaCancelacion,
    BigDecimal cargo,
    BigDecimal reembolso
) {}