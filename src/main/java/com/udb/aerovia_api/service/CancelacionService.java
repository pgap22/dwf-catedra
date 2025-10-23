package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Cancelacion;
import com.udb.aerovia_api.domain.Pago;
import com.udb.aerovia_api.domain.Reserva;
import com.udb.aerovia_api.domain.ReservaAsiento;
import com.udb.aerovia_api.domain.TarifaOperacion;
import com.udb.aerovia_api.domain.enums.EstadoReserva;
import com.udb.aerovia_api.domain.enums.Rol;
import com.udb.aerovia_api.dto.CancelacionDto;
import com.udb.aerovia_api.dto.CreateCancelacionDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.CancelacionMapper;
import com.udb.aerovia_api.repository.CancelacionRepository;
import com.udb.aerovia_api.repository.PagoRepository;
import com.udb.aerovia_api.repository.ReservaAsientoRepository;
import com.udb.aerovia_api.repository.ReservaRepository;
import com.udb.aerovia_api.repository.TarifaOperacionRepository;
import com.udb.aerovia_api.repository.UsuarioRepository;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CancelacionService {

    private final CancelacionRepository cancelacionRepository;
    private final ReservaRepository reservaRepository;
    private final CancelacionMapper cancelacionMapper;
    private final PagoRepository pagoRepository;
    private final ReservaAsientoRepository reservaAsientoRepository;
    private final TarifaOperacionRepository tarifaOperacionRepository;

    public CancelacionService(CancelacionRepository cancelacionRepository,
            ReservaRepository reservaRepository,
            CancelacionMapper cancelacionMapper,
            PagoRepository pagoRepository, // Añadir
            ReservaAsientoRepository reservaAsientoRepository, // Añadir
            TarifaOperacionRepository tarifaOperacionRepository, // Añadir
            UsuarioRepository usuarioRepository /* Añadir */) {
        this.cancelacionRepository = cancelacionRepository;
        this.reservaRepository = reservaRepository;
        this.cancelacionMapper = cancelacionMapper;
        this.pagoRepository = pagoRepository;
        this.reservaAsientoRepository = reservaAsientoRepository;
        this.tarifaOperacionRepository = tarifaOperacionRepository;
    }

    @Transactional
    public CancelacionDto cancelarReserva(CreateCancelacionDto dto) {
        Reserva reserva = reservaRepository.findById(dto.reservaId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", dto.reservaId()));

        // --- Verificación de Permisos ---
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuarioAutenticado = authentication.getName();
        boolean esAdminOAgente = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + Rol.ADMIN.name())) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Rol.AGENTE.name()));

        if (!reserva.getUsuario().getCorreo().equals(correoUsuarioAutenticado) && !esAdminOAgente) {
            throw new AccessDeniedException("No tiene permiso para cancelar esta reserva.");
        }
        // --- Fin Verificación de Permisos ---

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("La reserva con ID " + dto.reservaId() + " ya ha sido cancelada.");
        }
        if (reserva.getEstado() == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("No se puede cancelar una reserva completada.");
        }

        // Lógica Simplificada - ¡MEJORAR CON REGLAS REALES BASADAS EN FECHA/TARIFA!
        // 1. Calcular cargo (Ejemplo: 0 si tarifa es reembolsable, 100% si no)
        // Necesitamos cargar los detalles de los asientos para ver la tarifa
        List<ReservaAsiento> asientosReservados = reservaAsientoRepository.findByReservaId(reserva.getId());
        boolean algunaTarifaNoReembolsable = asientosReservados.stream()
                .anyMatch(ra -> !ra.getTarifaOperacion().getTarifa().isReembolsable());

        BigDecimal cargoCalculado = algunaTarifaNoReembolsable ? reserva.getTotal() : BigDecimal.ZERO;

        // 2. Calcular total pagado (sumando pagos reales)
        BigDecimal totalPagado = pagoRepository.findByReservaId(reserva.getId()).stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 3. Calcular reembolso
        BigDecimal reembolsoCalculado = totalPagado.subtract(cargoCalculado).max(BigDecimal.ZERO);

        // --- LIBERAR ASIENTOS ---
        List<TarifaOperacion> tarifasAActualizar = new ArrayList<>();
        for (ReservaAsiento ra : asientosReservados) {
            TarifaOperacion tarifaOp = ra.getTarifaOperacion();
            // Es crucial recargar la entidad para asegurar que tenemos la última versión
            // si hubo concurrencia, aunque aquí simplificamos sin bloqueo explícito.
            TarifaOperacion tarifaOpActual = tarifaOperacionRepository.findById(tarifaOp.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("TarifaOperacion", "id", tarifaOp.getId())); // O
                                                                                                                  // manejar
                                                                                                                  // de
                                                                                                                  // otra
                                                                                                                  // forma

            tarifaOpActual.setAsientosDisponibles(tarifaOpActual.getAsientosDisponibles() + 1);
            tarifasAActualizar.add(tarifaOpActual);
        }
        tarifaOperacionRepository.saveAll(tarifasAActualizar);
        // --- FIN LIBERAR ASIENTOS ---

        // Actualizar estado de la reserva
        reserva.setEstado(EstadoReserva.CANCELADA);
        reservaRepository.save(reserva);

        // Crear registro de cancelación
        Cancelacion nuevaCancelacion = new Cancelacion();
        nuevaCancelacion.setReserva(reserva);
        nuevaCancelacion.setFechaCancelacion(LocalDateTime.now());
        nuevaCancelacion.setCargo(cargoCalculado);
        nuevaCancelacion.setReembolso(reembolsoCalculado);

        return cancelacionMapper.toDto(cancelacionRepository.save(nuevaCancelacion));
    }

    @Transactional(readOnly = true)
    public CancelacionDto obtenerCancelacionPorReservaId(Long reservaId) {
        // Validar permisos (similar a obtenerReservaPorCodigo)
        Reserva reserva = reservaRepository.findById(reservaId)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", reservaId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuarioAutenticado = authentication.getName();
        boolean esAdminOAgente = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + Rol.ADMIN.name())) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Rol.AGENTE.name()));
        if (!reserva.getUsuario().getCorreo().equals(correoUsuarioAutenticado) && !esAdminOAgente) {
            throw new AccessDeniedException("No tiene permiso para ver esta cancelación.");
        }

        return cancelacionRepository.findByReservaId(reservaId)
                .map(cancelacionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró cancelación para la Reserva",
                        "reservaId", reservaId));
    }

    @Transactional(readOnly = true)
    public CancelacionDto obtenerCancelacionPorId(Long id) {
        return cancelacionRepository.findById(id)
                .map(cancelacionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Cancelacion", "id", id));
    }

    @Transactional(readOnly = true)
    public List<CancelacionDto> obtenerTodasCancelaciones() {
        return cancelacionRepository.findAll().stream()
                .map(cancelacionMapper::toDto)
                .collect(Collectors.toList());
    }
}