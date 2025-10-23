package com.udb.aerovia_api.dto;

import com.udb.aerovia_api.domain.enums.Rol;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(name = "CreateUserAdminDto", description = "Payload para registrar usuarios con rol administrativo")
public record CreateUserAdminDto(
        @Schema(description = "Nombre completo del usuario", example = "Ana Morales", maxLength = 150)
        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 150, message = "El nombre debe tener maximo 150 caracteres.")
        String nombre,

        @Schema(description = "Correo corporativo del usuario", example = "ana.morales@aerovia.test", maxLength = 180)
        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El correo no tiene un formato valido.")
        @Size(max = 180, message = "El correo debe tener maximo 180 caracteres.")
        String correo,

        @Schema(description = "Contrasena temporal que se entregara al usuario", example = "Admin123!", minLength = 8)
        @NotBlank(message = "La contrasena es obligatoria.")
        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres.")
        String password,

        @Schema(description = "Rol que determina los permisos del usuario", implementation = Rol.class, example = "ADMIN")
        @NotNull(message = "El rol es obligatorio.")
        Rol rol,

        @Schema(description = "Indica si el usuario puede autenticarse en el sistema", example = "true")
        @NotNull(message = "El estado es obligatorio.")
        Boolean activo
) {
}

