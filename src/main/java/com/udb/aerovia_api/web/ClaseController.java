package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.ClaseDto;
import com.udb.aerovia_api.dto.CreateClaseDto;
import com.udb.aerovia_api.service.ClaseService;
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
@RequestMapping("/api/v1/clases")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clases", description = "Catalogo de clases de servicio (Rol requerido: ADMIN)")
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    @Operation(
            summary = "Crear clase",
            description = "Registra una nueva clase de servicio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Clase creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<ClaseDto> createClase(@Valid @RequestBody CreateClaseDto createDto) {
        return new ResponseEntity<>(claseService.crearClase(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar clases",
            description = "Obtiene todas las clases registradas.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ClaseDto>> getAllClases() {
        return ResponseEntity.ok(claseService.obtenerTodas());
    }

    @Operation(
            summary = "Consultar clase",
            description = "Obtiene los detalles de una clase.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase encontrada"),
            @ApiResponse(responseCode = "404", description = "Clase no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ClaseDto> getClaseById(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar clase",
            description = "Modifica la informacion de una clase de servicio."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Clase actualizada"),
            @ApiResponse(responseCode = "404", description = "Clase no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ClaseDto> updateClase(@PathVariable Long id, @Valid @RequestBody CreateClaseDto updateDto) {
        return ResponseEntity.ok(claseService.actualizarClase(id, updateDto));
    }

    @Operation(
            summary = "Eliminar clase",
            description = "Elimina una clase del catalogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Clase eliminada"),
            @ApiResponse(responseCode = "404", description = "Clase no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClase(@PathVariable Long id) {
        claseService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }
}

