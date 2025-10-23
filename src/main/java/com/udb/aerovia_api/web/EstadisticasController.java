package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.EstadisticasDto;
import com.udb.aerovia_api.service.EstadisticasService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/estadisticas")
@PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Estadisticas", description = "Indicadores operativos del sistema")
public class EstadisticasController {

    private final EstadisticasService estadisticasService;

    public EstadisticasController(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

    @Operation(
            summary = "Obtener estadisticas basicas",
            description = "Devuelve contadores de reservas por estado y cancelaciones realizadas."
    )
    @ApiResponse(responseCode = "200", description = "Estadisticas generadas correctamente")
    @GetMapping
    public ResponseEntity<EstadisticasDto> getEstadisticasBasicas() {
        EstadisticasDto estadisticas = estadisticasService.obtenerEstadisticasBasicas();
        return ResponseEntity.ok(estadisticas);
    }
}
