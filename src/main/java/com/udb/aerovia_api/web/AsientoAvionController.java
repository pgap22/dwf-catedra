package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.AsientoAvionDto;
import com.udb.aerovia_api.dto.CreateAsientoAvionDto;
import com.udb.aerovia_api.service.AsientoAvionService;
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
@RequestMapping("/api/v1/asientos-avion")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Asientos de avion", description = "Configuracion de asientos por aeronave (Rol requerido: ADMIN)")
public class AsientoAvionController {

    private final AsientoAvionService service;

    public AsientoAvionController(AsientoAvionService service) {
        this.service = service;
    }

    @Operation(
            summary = "Crear asiento",
            description = "Registra un nuevo asiento dentro de un avion especifico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asiento creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "El asiento ya existe para el avion")
    })
    @PostMapping
    public ResponseEntity<AsientoAvionDto> createAsiento(@Valid @RequestBody CreateAsientoAvionDto dto) {
        return new ResponseEntity<>(service.crearAsiento(dto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar asientos por avion",
            description = "Devuelve los asientos configurados para un avion concreto.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/avion/{avionId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AsientoAvionDto>> getAsientosPorAvion(@PathVariable Long avionId) {
        return ResponseEntity.ok(service.obtenerAsientosPorAvion(avionId));
    }

    @Operation(
            summary = "Consultar asiento",
            description = "Obtiene la informacion de un asiento individual.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asiento encontrado"),
            @ApiResponse(responseCode = "404", description = "Asiento no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AsientoAvionDto> getAsientoById(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerAsientoPorId(id));
    }

    @Operation(
            summary = "Actualizar asiento",
            description = "Modifica la informacion de un asiento existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Asiento actualizado"),
            @ApiResponse(responseCode = "404", description = "Asiento no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AsientoAvionDto> updateAsiento(@PathVariable Long id, @Valid @RequestBody CreateAsientoAvionDto dto) {
        return ResponseEntity.ok(service.actualizarAsiento(id, dto));
    }

    @Operation(
            summary = "Eliminar asiento",
            description = "Elimina un asiento del inventario del avion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Asiento eliminado"),
            @ApiResponse(responseCode = "404", description = "Asiento no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAsiento(@PathVariable Long id) {
        service.eliminarAsiento(id);
        return ResponseEntity.noContent().build();
    }
}

