package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.AsignarTripulanteDto;
import com.udb.aerovia_api.dto.OperacionTripulacionDto;
import com.udb.aerovia_api.service.OperacionTripulacionService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/operaciones-tripulacion")
@PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Operaciones de vuelo", description = "Asignacion de tripulaciones a operaciones")
public class OperacionTripulacionController {

    private final OperacionTripulacionService service;

    public OperacionTripulacionController(OperacionTripulacionService service) {
        this.service = service;
    }

    @Operation(summary = "Asignar tripulante", description = "Relaciona un tripulante con una operacion de vuelo en un rol especifico.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Asignacion creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Operacion o tripulante no existe"),
            @ApiResponse(responseCode = "409", description = "Tripulante ya asignado a la operacion")
    })
    @PostMapping
    public ResponseEntity<OperacionTripulacionDto> asignarTripulante(@Valid @RequestBody AsignarTripulanteDto dto) {
        return new ResponseEntity<>(service.asignarTripulante(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Desasignar tripulante", description = "Elimina la relacion de un tripulante con una operacion de vuelo.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Asignacion eliminada"),
            @ApiResponse(responseCode = "404", description = "Asignacion no existe")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desasignarTripulante(@PathVariable Long id) {
        service.desasignarTripulante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/operacion-vuelo/{operacionVueloId}")
    public ResponseEntity<List<OperacionTripulacionDto>> listarTripulatesByOperacionId(@PathVariable Long operacionVueloId) {
        return ResponseEntity.ok(service.listarTripulatesByOperacionId(operacionVueloId));
    }
}
