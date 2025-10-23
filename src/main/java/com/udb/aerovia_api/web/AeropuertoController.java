package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.AeropuertoDto;
import com.udb.aerovia_api.dto.CreateAeropuertoDto;
import com.udb.aerovia_api.service.AeropuertoService;
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
@RequestMapping("/api/v1/aeropuertos")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Aeropuertos", description = "Gestion de aeropuertos y ubicaciones (Rol requerido: ADMIN)")
public class AeropuertoController {

    private final AeropuertoService aeropuertoService;

    public AeropuertoController(AeropuertoService aeropuertoService) {
        this.aeropuertoService = aeropuertoService;
    }

    @Operation(
            summary = "Crear aeropuerto",
            description = "Registra un nuevo aeropuerto con su informacion geografica."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Aeropuerto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "409", description = "Codigo IATA duplicado")
    })
    @PostMapping
    public ResponseEntity<AeropuertoDto> createAeropuerto(@Valid @RequestBody CreateAeropuertoDto createDto) {
        return new ResponseEntity<>(aeropuertoService.crearAeropuerto(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar aeropuertos",
            description = "Devuelve el catalogo completo de aeropuertos registrados.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AeropuertoDto>> getAllAeropuertos() {
        return ResponseEntity.ok(aeropuertoService.obtenerTodos());
    }

    @Operation(
            summary = "Consultar aeropuerto",
            description = "Obtiene los detalles de un aeropuerto especifico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aeropuerto encontrado"),
            @ApiResponse(responseCode = "404", description = "Aeropuerto no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AeropuertoDto> getAeropuertoById(@PathVariable Long id) {
        return ResponseEntity.ok(aeropuertoService.obtenerPorId(id));
    }

    @Operation(
            summary = "Actualizar aeropuerto",
            description = "Modifica los datos de un aeropuerto existente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Aeropuerto actualizado"),
            @ApiResponse(responseCode = "404", description = "Aeropuerto no existe")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AeropuertoDto> updateAeropuerto(@PathVariable Long id, @Valid @RequestBody CreateAeropuertoDto updateDto) {
        return ResponseEntity.ok(aeropuertoService.actualizarAeropuerto(id, updateDto));
    }

    @Operation(
            summary = "Eliminar aeropuerto",
            description = "Elimina un aeropuerto del catalogo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Aeropuerto eliminado"),
            @ApiResponse(responseCode = "404", description = "Aeropuerto no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAeropuerto(@PathVariable Long id) {
        aeropuertoService.eliminarAeropuerto(id);
        return ResponseEntity.noContent().build();
    }
}

