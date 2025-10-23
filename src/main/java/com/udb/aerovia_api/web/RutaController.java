package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateRutaDto;
import com.udb.aerovia_api.dto.RutaDto;
import com.udb.aerovia_api.dto.UpdateRutaDto;
import com.udb.aerovia_api.service.RutaService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rutas")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Rutas", description = "Catalogo de rutas aereas (Rol requerido: ADMIN)")
public class RutaController {

    private final RutaService rutaService;

    public RutaController(RutaService rutaService) {
        this.rutaService = rutaService;
    }

    @Operation(
            summary = "Crear ruta",
            description = "Registra una nueva ruta entre aeropuertos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ruta creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Aeropuertos relacionados no existen")
    })
    @PostMapping
    public ResponseEntity<RutaDto> createRuta(@Valid @RequestBody CreateRutaDto createDto) {
        return new ResponseEntity<>(rutaService.crearRuta(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar rutas",
            description = "Obtiene todas las rutas registradas.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<RutaDto>> getAllRutas() {
        return ResponseEntity.ok(rutaService.obtenerTodas());
    }

    @Operation(
            summary = "Consultar ruta",
            description = "Obtiene los detalles de una ruta especifica.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ruta encontrada"),
            @ApiResponse(responseCode = "404", description = "Ruta no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RutaDto> getRutaById(@PathVariable Long id) {
        return ResponseEntity.ok(rutaService.obtenerPorId(id));
    }

    @Operation(
            summary = "Eliminar ruta",
            description = "Elimina una ruta del catalogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Ruta eliminada"),
            @ApiResponse(responseCode = "404", description = "Ruta no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRuta(@PathVariable Long id) {
        rutaService.eliminarRuta(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar ruta",
            description = "Modifica los aeropuertos o la distancia de una ruta existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ruta actualizada"),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida"),
            @ApiResponse(responseCode = "404", description = "Ruta o aeropuertos relacionados no existen")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RutaDto> updateRuta(@PathVariable Long id, @Valid @RequestBody UpdateRutaDto updateDto) {
        return ResponseEntity.ok(rutaService.actualizarRuta(id, updateDto));
    }
}

