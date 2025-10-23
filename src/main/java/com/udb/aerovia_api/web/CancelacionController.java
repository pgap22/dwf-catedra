package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CancelacionDto;
import com.udb.aerovia_api.dto.CreateCancelacionDto;
import com.udb.aerovia_api.service.CancelacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cancelaciones")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservas", description = "Gestion de reservas, cancelaciones y pagos")
public class CancelacionController {

    private final CancelacionService cancelacionService;

    public CancelacionController(CancelacionService cancelacionService) {
        this.cancelacionService = cancelacionService;
    }

    @Operation(
            summary = "Cancelar reserva",
            description = "Genera la cancelacion de una reserva y calcula cargos y reembolsos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva cancelada"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada"),
            @ApiResponse(responseCode = "409", description = "Reserva con estado no cancelable")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CancelacionDto> cancelarReserva(@Valid @RequestBody CreateCancelacionDto dto) {
        return ResponseEntity.ok(cancelacionService.cancelarReserva(dto));
    }

    @Operation(
            summary = "Consultar cancelacion por reserva",
            description = "Recupera los detalles de cancelacion asociados a una reserva.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancelacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Cancelacion no existe")
    })
    @GetMapping("/reserva/{reservaId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CancelacionDto> getCancelacionByReservaId(@PathVariable Long reservaId) {
        return ResponseEntity.ok(cancelacionService.obtenerCancelacionPorReservaId(reservaId));
    }

    @Operation(
            summary = "Consultar cancelacion",
            description = "Obtiene los detalles de una cancelacion especifica.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cancelacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Cancelacion no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<CancelacionDto> getCancelacionById(@PathVariable Long id) {
        return ResponseEntity.ok(cancelacionService.obtenerCancelacionPorId(id));
    }

    @Operation(
            summary = "Listar cancelaciones",
            description = "Lista todas las cancelaciones registradas.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<CancelacionDto>> getAllCancelaciones() {
        return ResponseEntity.ok(cancelacionService.obtenerTodasCancelaciones());
    }
}
