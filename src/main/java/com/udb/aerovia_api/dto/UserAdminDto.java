package com.udb.aerovia_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "UserAdminDto", description = "Datos expuestos para la administracion de usuarios")
public record UserAdminDto(
        @Schema(description = "Identificador unico del usuario", example = "42")
        Long id,

        @Schema(description = "Nombre completo del usuario", example = "Ana Morales")
        String nombre,

        @Schema(description = "Correo electronico institucional", example = "ana.morales@aerovia.test")
        String correo,

        @Schema(description = "Rol asignado", example = "ADMIN")
        String rol,

        @Schema(description = "Estado actual del usuario", example = "true")
        boolean activo
) {
}

