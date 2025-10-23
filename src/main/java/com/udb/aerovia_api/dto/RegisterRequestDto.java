package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

    @NotBlank(message = "El nombre no puede estar vacío.")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres.")
    String nombre,

    @NotBlank(message = "El correo no puede estar vacío.")
    @Email(message = "El formato del correo electrónico no es válido.")
    String correo,

    @NotBlank(message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres.")
    String password
) {}