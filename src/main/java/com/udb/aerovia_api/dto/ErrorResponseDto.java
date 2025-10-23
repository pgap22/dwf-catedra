package com.udb.aerovia_api.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ErrorResponse", description = "Formato estandar de error para todas las respuestas fallidas")
public record ErrorResponseDto(
    @Schema(description = "Fecha y hora del error en formato ISO-8601", example = "2025-10-22T18:30:00.123")
    LocalDateTime timestamp,

    @Schema(description = "Codigo HTTP devuelto", example = "403")
    int status,

    @Schema(description = "Nombre corto del error", example = "FORBIDDEN")
    String error,

    @Schema(description = "Mensaje funcional orientado al usuario", example = "El usuario no tiene permisos para acceder a este recurso")
    String message,

    @Schema(description = "Ruta del endpoint que genero el error", example = "/api/v1/usuarios")
    String path,

    @Schema(description = "Detalle de validaciones fallidas, cuando aplique", example = "{\"correo\":\"El correo ya esta en uso\"}", nullable = true)
    Map<String, String> details
) {
    // Constructor auxiliar para crear errores sin el campo "details"
    public ErrorResponseDto(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }
}

