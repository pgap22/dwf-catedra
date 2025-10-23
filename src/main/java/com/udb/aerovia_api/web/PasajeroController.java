package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreatePasajeroDto;
import com.udb.aerovia_api.dto.PasajeroDto;
import com.udb.aerovia_api.service.PasajeroService;
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
@RequestMapping("/api/v1/pasajeros")
@PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Pasajeros", description = "Gestion de datos de pasajeros (Rol requerido: ADMIN)")
public class PasajeroController {

    private final PasajeroService pasajeroService;

    public PasajeroController(PasajeroService pasajeroService) {
        this.pasajeroService = pasajeroService;
    }

    @Operation(
            summary = "Crear pasajero",
            description = "Registra un nuevo pasajero."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pasajero creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<PasajeroDto> createPasajero(@Valid @RequestBody CreatePasajeroDto createDto) {
        return new ResponseEntity<>(pasajeroService.crearPasajero(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar pasajeros",
            description = "Obtiene el catalogo de pasajeros registrados.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public ResponseEntity<List<PasajeroDto>> getAllPasajeros() {
        return ResponseEntity.ok(pasajeroService.obtenerTodos());
    }

    @Operation(
            summary = "Consultar pasajero",
            description = "Obtiene la informacion de un pasajero especifico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pasajero encontrado"),
            @ApiResponse(responseCode = "404", description = "Pasajero no existe")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PasajeroDto> getPasajeroById(@PathVariable Long id) {
        return ResponseEntity.ok(pasajeroService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar pasajero",
            description = "Modifica la informacion de un pasajero."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pasajero actualizado"),
            @ApiResponse(responseCode = "404", description = "Pasajero no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PasajeroDto> updatePasajero(@PathVariable Long id, @Valid @RequestBody CreatePasajeroDto updateDto) {
        return ResponseEntity.ok(pasajeroService.actualizarPasajero(id, updateDto));
    }

    @Operation(
            summary = "Eliminar pasajero",
            description = "Elimina un pasajero del registro."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pasajero eliminado"),
            @ApiResponse(responseCode = "404", description = "Pasajero no existe")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePasajero(@PathVariable Long id) {
        pasajeroService.eliminarPasajero(id);
        return ResponseEntity.noContent().build();
    }
}

