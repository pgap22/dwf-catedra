package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateTarifaDto;
import com.udb.aerovia_api.dto.TarifaDto;
import com.udb.aerovia_api.service.TarifaService;
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
@RequestMapping("/api/v1/tarifas")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tarifas", description = "Definicion de tarifas base por clase (Rol requerido: ADMIN)")
public class TarifaController {

    private final TarifaService tarifaService;

    public TarifaController(TarifaService tarifaService) {
        this.tarifaService = tarifaService;
    }

    @Operation(
            summary = "Crear tarifa",
            description = "Registra una tarifa con codigo, clase y tipo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarifa creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Codigo de tarifa duplicado")
    })
    @PostMapping
    public ResponseEntity<TarifaDto> createTarifa(@Valid @RequestBody CreateTarifaDto dto) {
        return new ResponseEntity<>(tarifaService.crearTarifa(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar tarifas",
            description = "Devuelve todas las tarifas disponibles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<TarifaDto>> getAllTarifas() {
        return ResponseEntity.ok(tarifaService.obtenerTodas());
    }

    @Operation(
            summary = "Consultar tarifa",
            description = "Obtiene los detalles de una tarifa especifica.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarifa no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<TarifaDto> getTarifaById(@PathVariable Long id) {
        return ResponseEntity.ok(tarifaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar tarifa",
            description = "Modifica el detalle de una tarifa existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarifa actualizada"),
            @ApiResponse(responseCode = "404", description = "Tarifa no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TarifaDto> updateTarifa(@PathVariable Long id, @Valid @RequestBody CreateTarifaDto dto) {
        return ResponseEntity.ok(tarifaService.actualizarTarifa(id, dto));
    }

    @Operation(
            summary = "Eliminar tarifa",
            description = "Elimina una tarifa del catalogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarifa eliminada"),
            @ApiResponse(responseCode = "404", description = "Tarifa no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarifa(@PathVariable Long id) {
        tarifaService.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }
}

