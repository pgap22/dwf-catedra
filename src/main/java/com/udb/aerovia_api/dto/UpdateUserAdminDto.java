package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

@Schema(name = "UpdateUserAdminDto", description = "Payload para actualizar datos o rol de un usuario")
public record UpdateUserAdminDto(
        @Schema(description = "Nombre completo del usuario", example = "Ana Morales", maxLength = 150, nullable = true)
        @Size(max = 150, message = "El nombre debe tener maximo 150 caracteres.")
        String nombre,

        @Schema(description = "Correo electronico a actualizar", example = "ana.morales@aerovia.test", maxLength = 180, nullable = true)
        @Email(message = "El correo no tiene un formato valido.")
        @Size(max = 180, message = "El correo debe tener maximo 180 caracteres.")
        String correo,

        @Schema(description = "Nueva contrasena. Dejar vacio si no se modificara.", example = "Admin987!", minLength = 8, nullable = true)
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres.")
        String password,

        @Schema(description = "Rol actualizado para el usuario", implementation = Rol.class, example = "AGENTE", nullable = true)
        Rol rol
) {
}


