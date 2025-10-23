package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateVueloDto;
import com.udb.aerovia_api.dto.VueloDto;
import com.udb.aerovia_api.service.VueloService;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/vuelos")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Operaciones de vuelo", description = "Gestion de catalogo de vuelos base")
public class VueloController {

    private final VueloService vueloService;

    public VueloController(VueloService vueloService) {
        this.vueloService = vueloService;
    }

    @Operation(
            summary = "Crear vuelo base",
            description = "Registra un vuelo asociado a una aerolinea y una ruta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vuelo creado"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Aerolinea o ruta no existe"),
            @ApiResponse(responseCode = "409", description = "Numero de vuelo duplicado para la aerolinea")
    })
    @PostMapping
    public ResponseEntity<VueloDto> createVuelo(@Valid @RequestBody CreateVueloDto createDto) {
        return new ResponseEntity<>(vueloService.crearVuelo(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar vuelos base",
            description = "Obtiene el listado de vuelos registrados.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<VueloDto>> getAllVuelos() {
        return ResponseEntity.ok(vueloService.obtenerTodos());
    }

    @Operation(
            summary = "Consultar vuelo base",
            description = "Obtiene la informacion de un vuelo especifico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vuelo encontrado"),
            @ApiResponse(responseCode = "404", description = "Vuelo no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<VueloDto> getVueloById(@PathVariable Long id) {
        return ResponseEntity.ok(vueloService.obtenerPorId(id));
    }

    @Operation(
            summary = "Eliminar vuelo base",
            description = "Elimina un vuelo del catalogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vuelo eliminado"),
            @ApiResponse(responseCode = "404", description = "Vuelo no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVuelo(@PathVariable Long id) {
        vueloService.eliminarVuelo(id);
        return ResponseEntity.noContent().build();
    }
}
