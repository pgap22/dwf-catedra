package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.CreatePagoDto;
import com.udb.aerovia_api.dto.PagoDto;
import com.udb.aerovia_api.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pagos")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Reservas", description = "Gestion de reservas, cancelaciones y pagos (Roles: ADMIN o AGENTE para operaciones generales; listado global solo ADMIN)")
public class PagoController {

    private final PagoService pagoService;

    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    @Operation(
            summary = "Registrar pago",
            description = "Registra un pago asociado a una reserva."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pago registrado"),
            @ApiResponse(responseCode = "404", description = "Reserva no existe"),
            @ApiResponse(responseCode = "400", description = "Datos invalidos")
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<PagoDto> registerPago(@Valid @RequestBody CreatePagoDto createDto) {
        return new ResponseEntity<>(pagoService.registrarPago(createDto), HttpStatus.CREATED);
    }

    @Operation(
            summary = "Listar pagos",
            description = "Disponible para rol ADMIN. Devuelve los pagos registrados con filtros opcionales por reserva y fechas.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "401", description = "No autenticado"),
            @ApiResponse(responseCode = "403", description = "No autorizado")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PagoDto>> getPagosAdmin(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "fechaPago") String sort,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction,
            @RequestParam(required = false) Long reservaId,
            @RequestParam(required = false) String codigoReserva,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Page<PagoDto> result = pagoService.buscarPagosAdmin(pageable,
                Optional.ofNullable(reservaId),
                Optional.ofNullable(codigoReserva),
                Optional.ofNullable(fechaDesde),
                Optional.ofNullable(fechaHasta));
        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "Listar pagos por reserva",
            description = "Obtiene todos los pagos realizados para una reserva especifica.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
            @ApiResponse(responseCode = "404", description = "Reserva no existe")
    })
    @GetMapping("/reserva/{reservaId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<List<PagoDto>> getPagosByReserva(@PathVariable Long reservaId) {
        return ResponseEntity.ok(pagoService.obtenerPagosPorReserva(reservaId));
    }

    @Operation(
            summary = "Consultar pago",
            description = "Obtiene los detalles de un pago individual.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pago encontrado"),
            @ApiResponse(responseCode = "404", description = "Pago no existe")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AGENTE')")
    public ResponseEntity<PagoDto> getPagoById(@PathVariable Long id) {
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }
}

