package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateTripulanteDto;
import com.udb.aerovia_api.dto.TripulanteDto;
import com.udb.aerovia_api.service.TripulanteService;
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
@RequestMapping("/api/v1/tripulantes")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Tripulantes", description = "Gestion del personal de vuelo (Rol requerido: ADMIN salvo consultas marcadas)")
public class TripulanteController {

    private final TripulanteService tripulanteService;

    public TripulanteController(TripulanteService tripulanteService) {
        this.tripulanteService = tripulanteService;
    }

    @Operation(
            summary = "Crear tripulante",
            description = "Registra un nuevo miembro de la tripulacion."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tripulante creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    public ResponseEntity<TripulanteDto> createTripulante(@Valid @RequestBody CreateTripulanteDto createDto) {
        return new ResponseEntity<>(tripulanteService.crearTripulante(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar tripulantes",
            description = "Obtiene todos los tripulantes registrados.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<TripulanteDto>> getAllTripulantes() {
        return ResponseEntity.ok(tripulanteService.obtenerTodos());
    }

    @Operation(
            summary = "Consultar tripulante",
            description = "Obtiene la informacion de un tripulante.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tripulante encontrado"),
            @ApiResponse(responseCode = "404", description = "Tripulante no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<TripulanteDto> getTripulanteById(@PathVariable Long id) {
        return ResponseEntity.ok(tripulanteService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar tripulante",
            description = "Modifica la informacion de un tripulante."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tripulante actualizado"),
            @ApiResponse(responseCode = "404", description = "Tripulante no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TripulanteDto> updateTripulante(@PathVariable Long id, @Valid @RequestBody CreateTripulanteDto updateDto) {
        return ResponseEntity.ok(tripulanteService.actualizarTripulante(id, updateDto));
    }

    @Operation(
            summary = "Eliminar tripulante",
            description = "Elimina a un tripulante del catalogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tripulante eliminado"),
            @ApiResponse(responseCode = "404", description = "Tripulante no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTripulante(@PathVariable Long id) {
        tripulanteService.eliminarTripulante(id);
        return ResponseEntity.noContent().build();
    }
}
