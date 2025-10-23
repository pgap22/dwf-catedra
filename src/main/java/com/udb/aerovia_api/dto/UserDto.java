package com.udb.aerovia_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserDto", description = "Datos basicos del usuario autenticado")
public record UserDto(
    @Schema(description = "Identificador del usuario", example = "42")
    Long id,

    @Schema(description = "Nombre completo", example = "Cliente Demo")
    String nombre,

    @Schema(description = "Correo registrado", example = "cliente@aerovia.test")
    String correo,

    @Schema(description = "Rol principal asignado", example = "CLIENTE")
    String rol
) {}
