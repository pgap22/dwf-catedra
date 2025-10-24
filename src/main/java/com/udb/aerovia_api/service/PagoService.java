package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Pago;
import com.udb.aerovia_api.domain.Reserva;
import com.udb.aerovia_api.domain.enums.EstadoReserva;
import com.udb.aerovia_api.dto.CreatePagoDto;
import com.udb.aerovia_api.dto.PagoDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.PagoMapper;
import com.udb.aerovia_api.repository.PagoRepository;
import com.udb.aerovia_api.repository.ReservaRepository;
import com.udb.aerovia_api.repository.specification.PagoSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final ReservaRepository reservaRepository;
    private final PagoMapper pagoMapper;

    public PagoService(PagoRepository pagoRepository, ReservaRepository reservaRepository, PagoMapper pagoMapper) {
        this.pagoRepository = pagoRepository;
        this.reservaRepository = reservaRepository;
        this.pagoMapper = pagoMapper;
    }

    @Transactional
    public PagoDto registrarPago(CreatePagoDto createDto) {
        Reserva reserva = reservaRepository.findById(createDto.reservaId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", createDto.reservaId()));

        if (reserva.getEstado() == EstadoReserva.CANCELADA) {
            throw new IllegalStateException("No es posible registrar pagos para una reserva cancelada.");
        }
        List<Pago> pagosPrevios = pagoRepository.findByReservaId(reserva.getId());
        BigDecimal totalPrevio = pagosPrevios.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalReserva = reserva.getTotal();

        if (totalReserva != null && totalPrevio.compareTo(totalReserva) >= 0) {
            if (reserva.getEstado() != EstadoReserva.COMPLETADA) {
                reserva.setEstado(EstadoReserva.COMPLETADA);
                reservaRepository.save(reserva);
            }
            throw new IllegalStateException("La reserva ya fue pagada totalmente.");
        }

        if (reserva.getEstado() == EstadoReserva.COMPLETADA) {
            throw new IllegalStateException("La reserva ya fue pagada.");
        }

        BigDecimal totalTrasPago = totalPrevio.add(createDto.monto());

        if (totalReserva != null && totalTrasPago.compareTo(totalReserva) > 0) {
            BigDecimal restante = totalReserva.subtract(totalPrevio).max(BigDecimal.ZERO);
            throw new IllegalArgumentException("El monto del pago excede el total de la reserva. Saldo pendiente: " + restante);
        }

        Pago nuevoPago = new Pago();
        nuevoPago.setReserva(reserva);
        nuevoPago.setFechaPago(createDto.fechaPago());
        nuevoPago.setMetodoPago(createDto.metodoPago());
        nuevoPago.setMonto(createDto.monto());

        Pago pagoGuardado = pagoRepository.save(nuevoPago);

        if (totalReserva == null || totalTrasPago.compareTo(totalReserva) >= 0) {
            reserva.setEstado(EstadoReserva.COMPLETADA);
            reservaRepository.save(reserva);
        }

        return pagoMapper.toDto(pagoGuardado);
    }

    @Transactional(readOnly = true)
    public List<PagoDto> obtenerPagosPorReserva(Long reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new ResourceNotFoundException("Reserva", "id", reservaId);
        }
        return pagoRepository.findByReservaId(reservaId).stream()
                .map(pagoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PagoDto obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id)
                .map(pagoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pago", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<PagoDto> buscarPagosAdmin(Pageable pageable,
            Optional<Long> reservaId,
            Optional<String> codigoReserva,
            Optional<LocalDate> fechaDesde,
            Optional<LocalDate> fechaHasta) {

        Specification<Pago> spec = null;
        spec = combine(spec, PagoSpecification.byReservaId(reservaId.orElse(null)));
        spec = combine(spec, PagoSpecification.byCodigoReserva(codigoReserva.map(String::trim).filter(s -> !s.isEmpty()).orElse(null)));
        spec = combine(spec, PagoSpecification.fechaDesde(fechaDesde.map(LocalDate::atStartOfDay).orElse(null)));
        spec = combine(spec, PagoSpecification.fechaHasta(fechaHasta.map(d -> d.atTime(23, 59, 59)).orElse(null)));

        Page<Pago> pagina = spec == null ? pagoRepository.findAll(pageable) : pagoRepository.findAll(spec, pageable);
        return pagina.map(pagoMapper::toDto);
    }

    private Specification<Pago> combine(Specification<Pago> base, Specification<Pago> other) {
        if (other == null) {
            return base;
        }
        return base == null ? Specification.where(other) : base.and(other);
    }
}
