package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.AerolineaDto;
import com.udb.aerovia_api.dto.CreateAerolineaDto;
import com.udb.aerovia_api.service.AerolineaService;
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
@RequestMapping("/api/v1/aerolineas")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Aerolineas", description = "Administracion de aerolineas (Rol requerido: ADMIN)")
public class AerolineaController {

    private final AerolineaService aerolineaService;

    public AerolineaController(AerolineaService aerolineaService) {
        this.aerolineaService = aerolineaService;
    }

    @Operation(
            summary = "Crear aerolinea",
            description = "Registra una nueva aerolinea con su nombre y codigo IATA."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aerolinea creada correctamente"),
            @ApiResponse(responseCode = "409", description = "Codigo IATA duplicado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<AerolineaDto> createAerolinea(@Valid @RequestBody CreateAerolineaDto createDto) {
        AerolineaDto nuevaAerolinea = aerolineaService.crearAerolinea(createDto);
        return new ResponseEntity<>(nuevaAerolinea, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar aerolineas",
            description = "Obtiene todas las aerolineas registradas.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AerolineaDto>> getAllAerolineas() {
        List<AerolineaDto> aerolineas = aerolineaService.obtenerTodasLasAerolineas();
        return ResponseEntity.ok(aerolineas);
    }

    @Operation(
            summary = "Consultar aerolinea",
            description = "Obtiene la informacion detallada de una aerolinea existente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aerolinea encontrada"),
            @ApiResponse(responseCode = "404", description = "Aerolinea no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AerolineaDto> getAerolineaById(@PathVariable Long id) {
        AerolineaDto aerolinea = aerolineaService.obtenerAerolineaPorId(id);
        return ResponseEntity.ok(aerolinea);
    }

    @Operation(
            summary = "Actualizar aerolinea",
            description = "Modifica los datos principales de una aerolinea."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aerolinea actualizada"),
            @ApiResponse(responseCode = "404", description = "Aerolinea no existe"),
            @ApiResponse(responseCode = "409", description = "Codigo IATA duplicado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AerolineaDto> updateAerolinea(@PathVariable Long id,
            @Valid @RequestBody CreateAerolineaDto updateDto) {
        AerolineaDto aerolineaActualizada = aerolineaService.actualizarAerolinea(id, updateDto);
        return ResponseEntity.ok(aerolineaActualizada);
    }

    @Operation(
            summary = "Eliminar aerolinea",
            description = "Elimina una aerolinea si no posee dependencias."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aerolinea eliminada"),
            @ApiResponse(responseCode = "404", description = "Aerolinea no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAerolinea(@PathVariable Long id) {
        aerolineaService.eliminarAerolinea(id);
        return ResponseEntity.noContent().build();
    }
}

