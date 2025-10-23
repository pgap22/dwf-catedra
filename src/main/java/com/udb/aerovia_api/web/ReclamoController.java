package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateReclamoDto;
import com.udb.aerovia_api.dto.ReclamoDto;
import com.udb.aerovia_api.dto.UpdateReclamoEstadoDto;
import com.udb.aerovia_api.service.ReclamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reclamos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reclamos", description = "Gestion y seguimiento de reclamos de pasajeros")
public class ReclamoController {

    private final ReclamoService reclamoService;

    public ReclamoController(ReclamoService reclamoService) {
        this.reclamoService = reclamoService;
    }

    @Operation(
            summary = "Crear reclamo",
            description = "Registra un reclamo relacionado a una reserva y pasajero."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reclamo creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Reserva o pasajero no existe")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReclamoDto> createReclamo(@Valid @RequestBody CreateReclamoDto dto) {
        return new ResponseEntity<>(reclamoService.crearReclamo(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reclamos por reserva",
            description = "Devuelve los reclamos asociados a una reserva.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/reserva/{reservaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReclamoDto>> getReclamosByReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(reclamoService.obtenerReclamosPorReserva(reservaId));
    }

    @Operation(
            summary = "Listar reclamos por pasajero",
            description = "Devuelve los reclamos presentados por un pasajero.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/pasajero/{pasajeroId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ReclamoDto>> getReclamosByPasajero(@PathVariable Long pasajeroId) {
        return ResponseEntity.ok(reclamoService.obtenerReclamosPorPasajero(pasajeroId));
    }

    @Operation(
            summary = "Consultar reclamo",
            description = "Obtiene los detalles de un reclamo especifico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo encontrado"),
            @ApiResponse(responseCode = "404", description = "Reclamo no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReclamoDto> getReclamoById(@PathVariable Long id) {
        return ResponseEntity.ok(reclamoService.obtenerReclamoPorId(id));
    }

    @Operation(
            summary = "Actualizar estado del reclamo",
            description = "Actualiza el estado de seguimiento de un reclamo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reclamo actualizado"),
            @ApiResponse(responseCode = "404", description = "Reclamo no existe")
    })
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<ReclamoDto> updateReclamoEstado(@PathVariable Long id, @Valid @RequestBody UpdateReclamoEstadoDto dto) {
        return ResponseEntity.ok(reclamoService.actualizarEstadoReclamo(id, dto));
    }
}
