package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateTarifaOperacionDto;
import com.udb.aerovia_api.dto.TarifaOperacionDto;
import com.udb.aerovia_api.service.TarifaOperacionService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tarifas-operacion")
@PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tarifas por operacion", description = "Asignacion de precios y cupos por operacion de vuelo (Rol requerido: ADMIN)")
public class TarifaOperacionController {

    private final TarifaOperacionService service;

    public TarifaOperacionController(TarifaOperacionService service) {
        this.service = service;
    }

    @Operation(
            summary = "Asignar tarifa a operacion",
            description = "Crea o asigna una tarifa especifica a una operacion de vuelo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarifa asignada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Operacion o tarifa base no existe"),
            @ApiResponse(responseCode = "409", description = "Combinacion ya registrada")
    })
    @PostMapping
    public ResponseEntity<TarifaOperacionDto> asignarTarifaAOperacion(@Valid @RequestBody CreateTarifaOperacionDto dto) {
        return new ResponseEntity<>(service.asignarTarifaAOperacion(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar tarifas de una operacion",
            description = "Obtiene las tarifas activas para una operacion de vuelo.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Operacion no existe")
    })
    @GetMapping("/operacion/{operacionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TarifaOperacionDto>> getTarifasPorOperacion(@PathVariable Long operacionId) {
        return ResponseEntity.ok(service.obtenerTarifasPorOperacion(operacionId));
    }

    @Operation(
            summary = "Consultar tarifa de operacion",
            description = "Obtiene los detalles de una tarifa asociada a una operacion.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarifa no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TarifaOperacionDto> getTarifaOperacionById(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerTarifaOperacionPorId(id));
    }

    @Operation(
            summary = "Actualizar tarifa de operacion",
            description = "Modifica precio y cupo disponible de una tarifa aplicada a una operacion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Tarifa no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TarifaOperacionDto> updateTarifaOperacion(@PathVariable Long id, @Valid @RequestBody CreateTarifaOperacionDto dto) {
        return ResponseEntity.ok(service.actualizarTarifaOperacion(id, dto));
    }

    @Operation(
            summary = "Eliminar tarifa de operacion",
            description = "Elimina una tarifa aplicada a una operacion de vuelo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarifa eliminada"),
            @ApiResponse(responseCode = "404", description = "Tarifa no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifaOperacion(@PathVariable Long id) {
        service.eliminarTarifaDeOperacion(id);
        return ResponseEntity.noContent().build();
    }
}

