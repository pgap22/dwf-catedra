package com.udb.aerovia_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "JwtAuthResponseDto", description = "Respuesta estandar del login con el token JWT y datos del usuario")
public record JwtAuthResponseDto(
    @Schema(description = "Token JWT que debe enviarse en el header Authorization", example = "eyJhbGciOiJIUzI1NiJ9...")
    String accessToken,

    @Schema(description = "Datos del usuario autenticado")
    UserDto user
) {}
