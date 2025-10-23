package com.udb.aerovia_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreatePasajeroDto(
    @NotBlank(message = "El nombre completo no puede estar vacío.")
    @Size(max = 150)
    String nombreCompleto,

    @NotNull(message = "La fecha de nacimiento es obligatoria.")
    @Past(message = "La fecha de nacimiento debe ser en el pasado.")
    LocalDate fechaNacimiento,

    @Size(max = 30, message = "El número de pasaporte no debe exceder los 30 caracteres.")
    String nroPasaporte
) {}