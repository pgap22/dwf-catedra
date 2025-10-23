package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.BusquedaVueloParamsDto;
import com.udb.aerovia_api.dto.CreateOperacionVueloDto;
import com.udb.aerovia_api.dto.OperacionVueloDto;
import com.udb.aerovia_api.dto.VueloDisponibleDto;
import com.udb.aerovia_api.service.OperacionVueloService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/operaciones-vuelo")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Operaciones de vuelo", description = "Programacion y consulta de vuelos")
public class OperacionVueloController {

    private final OperacionVueloService operacionVueloService;

    public OperacionVueloController(OperacionVueloService operacionVueloService) {
        this.operacionVueloService = operacionVueloService;
    }

    @Operation(
            summary = "Programar operacion de vuelo",
            description = "Crea una nueva operacion de vuelo con avion, ruta y horarios."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Operacion programada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Vuelo o avion no existe")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<OperacionVueloDto> programarVuelo(@Valid @RequestBody CreateOperacionVueloDto createDto) {
        return new ResponseEntity<>(operacionVueloService.programarVuelo(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar operaciones de vuelo",
            description = "Devuelve todas las operaciones de vuelo registradas.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OperacionVueloDto>> getAllOperacionesVuelo() {
        return ResponseEntity.ok(operacionVueloService.obtenerTodas());
    }

    @Operation(
            summary = "Consultar operacion de vuelo",
            description = "Obtiene los detalles de una operacion especifica.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Operacion encontrada"),
            @ApiResponse(responseCode = "404", description = "Operacion no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OperacionVueloDto> getOperacionVueloById(@PathVariable Long id) {
        return ResponseEntity.ok(operacionVueloService.obtenerPorId(id));
    }

    @Operation(
            summary = "Eliminar operacion de vuelo",
            description = "Elimina una operacion programada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Operacion eliminada"),
            @ApiResponse(responseCode = "404", description = "Operacion no existe")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<Void> deleteOperacionVuelo(@PathVariable Long id) {
        operacionVueloService.eliminarOperacion(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Buscar vuelos disponibles",
            description = "Filtra operaciones de vuelo por ruta y fecha, devolviendo tarifas disponibles.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Busqueda realizada")
    @GetMapping("/buscar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VueloDisponibleDto>> buscarVuelos(@Valid BusquedaVueloParamsDto params) {
        List<VueloDisponibleDto> vuelos = operacionVueloService.buscarVuelosDisponibles(params)
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vuelos);
    }
}
