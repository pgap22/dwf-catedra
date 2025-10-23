package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreateReservaDto;
import com.udb.aerovia_api.dto.ErrorResponseDto;
import com.udb.aerovia_api.dto.ReservaAdminDto;
import com.udb.aerovia_api.dto.ReservaDetalleDto;
import com.udb.aerovia_api.dto.UpdateReservaDto;
import com.udb.aerovia_api.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/reservas")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservas", description = "Gestion de reservas, cancelaciones y pagos")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @Operation(
            summary = "Crear reserva",
            description = "Crea una nueva reserva asociando pasajeros, asientos y tarifas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reserva creada"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos"),
            @ApiResponse(responseCode = "404", description = "Recursos relacionados no existen"),
            @ApiResponse(responseCode = "409", description = "Conflicto con disponibilidad de asientos")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservaDetalleDto> createReserva(@Valid @RequestBody CreateReservaDto dto) {
        ReservaDetalleDto nuevaReserva = reservaService.crearReserva(dto);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar reservas (admin)",
            description = "Disponible para rol ADMIN. Devuelve reservas paginadas con filtros por estado, fechas, aerolinea y ruta.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado recuperado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaAdminDto.class),
                            examples = @ExampleObject(name = "ListadoReservasAdmin", value = """
                                    {
                                      "content": [
                                        {
                                          "id": 1,
                                          "codigoReserva": "ABC123",
                                          "estado": "ACTIVA",
                                          "fechaReserva": "2025-10-20T15:45:00",
                                          "total": 640.00,
                                          "cliente": "Cliente Demo",
                                          "correoCliente": "cliente@aerovia.test",
                                          "aerolinea": "AeroVia Express",
                                          "origen": "San Salvador",
                                          "destino": "Miami"
                                        }
                                      ],
                                      "pageable": {
                                        "pageNumber": 0,
                                        "pageSize": 20
                                      },
                                      "totalElements": 1,
                                      "totalPages": 1,
                                      "last": true,
                                      "first": true,
                                      "size": 20,
                                      "number": 0,
                                      "sort": {
                                        "empty": false,
                                        "sorted": true,
                                        "unsorted": false
                                      },
                                      "numberOfElements": 1,
                                      "empty": false
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservaAdminDto>> getReservasAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "fechaReserva") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) Long aerolineaId,
            @RequestParam(required = false) Long origenId,
            @RequestParam(required = false) Long destinoId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Optional<com.udb.aerovia_api.domain.enums.EstadoReserva> estadoEnum = parseEstado(estado);
        Page<ReservaAdminDto> result = reservaService.buscarReservasAdmin(pageable, estadoEnum,
                Optional.ofNullable(fechaDesde), Optional.ofNullable(fechaHasta),
                Optional.ofNullable(aerolineaId), Optional.ofNullable(origenId), Optional.ofNullable(destinoId));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Modificar reserva", description = "Permite actualizar asientos, pasajeros o tarifas de una reserva antes de 24 horas de la salida.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva modificada",
                    content = @Content(schema = @Schema(implementation = ReservaDetalleDto.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Reserva no encontrada",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Conflicto con disponibilidad",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdateReservaDto.class),
                    examples = @ExampleObject(name = "ModificarReserva", value = """
                            {
                              "cambios": [
                                {
                                  "reservaAsientoId": 12,
                                  "asientoAvionId": 215,
                                  "tarifaOperacionId": 5
                                }
                              ]
                            }
                            """)))
    @PatchMapping("/{codigoReserva}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservaDetalleDto> updateReserva(
            @PathVariable String codigoReserva,
            @Valid @RequestBody UpdateReservaDto dto) {
        return ResponseEntity.ok(reservaService.modificarReserva(codigoReserva, dto));
    }

    @Operation(
            summary = "Consultar reserva por codigo",
            description = "Obtiene los detalles completos de una reserva a partir de su codigo.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva encontrada"),
            @ApiResponse(responseCode = "403", description = "Sin permisos"),
            @ApiResponse(responseCode = "404", description = "Reserva no existe")
    })
    @GetMapping("/{codigoReserva}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ReservaDetalleDto> getReservaByCodigo(@PathVariable String codigoReserva) {
        ReservaDetalleDto reserva = reservaService.obtenerReservaPorCodigo(codigoReserva);
        return ResponseEntity.ok(reserva);
    }

    @Operation(
            summary = "Listar reservas del cliente",
            description = "Devuelve todas las reservas del usuario autenticado con rol cliente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping("/mis-reservas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ReservaDetalleDto>> getMisReservas() {
        List<ReservaDetalleDto> reservas = reservaService.obtenerMisReservas();
        return ResponseEntity.ok(reservas);
    }

    private Optional<com.udb.aerovia_api.domain.enums.EstadoReserva> parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(com.udb.aerovia_api.domain.enums.EstadoReserva.valueOf(estado.toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado invalido: " + estado);
        }
    }
}
