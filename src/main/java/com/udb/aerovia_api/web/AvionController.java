package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.AvionDto;
import com.udb.aerovia_api.dto.CreateAvionDto;
import com.udb.aerovia_api.service.AvionService;
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
@RequestMapping("/api/v1/aviones")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Aviones", description = "Gestion del inventario de aeronaves (Rol requerido: ADMIN)")
public class AvionController {

    private final AvionService avionService;

    public AvionController(AvionService avionService) {
        this.avionService = avionService;
    }

    @Operation(
            summary = "Crear avion",
            description = "Registra un nuevo avion con matricula, modelo y capacidad."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avion creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Matricula duplicada")
    })
    @PostMapping
    public ResponseEntity<AvionDto> createAvion(@Valid @RequestBody CreateAvionDto createDto) {
        return new ResponseEntity<>(avionService.crearAvion(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar aviones",
            description = "Obtiene todo el inventario de aviones registrados.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AvionDto>> getAllAviones() {
        return ResponseEntity.ok(avionService.obtenerTodos());
    }

    @Operation(
            summary = "Consultar avion",
            description = "Recupera la informacion de un avion especifico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avion encontrado"),
            @ApiResponse(responseCode = "404", description = "Avion no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AvionDto> getAvionById(@PathVariable Long id) {
        return ResponseEntity.ok(avionService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar avion",
            description = "Actualiza la informacion de un avion existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avion actualizado"),
            @ApiResponse(responseCode = "404", description = "Avion no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AvionDto> updateAvion(@PathVariable Long id, @Valid @RequestBody CreateAvionDto updateDto) {
        return ResponseEntity.ok(avionService.actualizarAvion(id, updateDto));
    }

    @Operation(
            summary = "Eliminar avion",
            description = "Elimina un avion del inventario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Avion eliminado"),
            @ApiResponse(responseCode = "404", description = "Avion no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvion(@PathVariable Long id) {
        avionService.eliminarAvion(id);
        return ResponseEntity.noContent().build();
    }
}

