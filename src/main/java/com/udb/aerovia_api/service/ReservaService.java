package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.*;
import com.udb.aerovia_api.domain.enums.EstadoReserva;
import com.udb.aerovia_api.domain.enums.Rol;
import com.udb.aerovia_api.dto.CreateReservaDto;
import com.udb.aerovia_api.dto.ItemReservaDto;
import com.udb.aerovia_api.dto.ReservaAdminDto;
import com.udb.aerovia_api.dto.OperacionVueloDto;
import com.udb.aerovia_api.dto.ReservaAsientoDetalleDto;
import com.udb.aerovia_api.dto.ReservaDetalleDto;
import com.udb.aerovia_api.dto.UpdateReservaDto;
import com.udb.aerovia_api.dto.UpdateReservaItemDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.OperacionVueloMapper;
import com.udb.aerovia_api.mapper.ReservaAsientoMapper;
import com.udb.aerovia_api.mapper.ReservaMapper;
import com.udb.aerovia_api.repository.AsientoAvionRepository;
import com.udb.aerovia_api.repository.OperacionVueloRepository;
import com.udb.aerovia_api.repository.PasajeroRepository;
import com.udb.aerovia_api.repository.ReservaAsientoRepository;
import com.udb.aerovia_api.repository.ReservaRepository;
import com.udb.aerovia_api.repository.TarifaOperacionRepository;
import com.udb.aerovia_api.repository.UsuarioRepository;
import com.udb.aerovia_api.repository.specification.ReservaSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservaService {

    private static final Logger logger = LoggerFactory.getLogger(ReservaService.class);

    private final ReservaRepository reservaRepository;
    private final ReservaAsientoRepository reservaAsientoRepository;
    private final OperacionVueloRepository operacionVueloRepository;
    private final PasajeroRepository pasajeroRepository;
    private final AsientoAvionRepository asientoAvionRepository;
    private final TarifaOperacionRepository tarifaOperacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReservaMapper reservaMapper;
    private final ReservaAsientoMapper reservaAsientoMapper;
    private final OperacionVueloMapper operacionVueloMapper;

    public ReservaService(ReservaRepository reservaRepository,
            ReservaAsientoRepository reservaAsientoRepository,
            OperacionVueloRepository operacionVueloRepository,
            PasajeroRepository pasajeroRepository,
            AsientoAvionRepository asientoAvionRepository,
            TarifaOperacionRepository tarifaOperacionRepository,
            UsuarioRepository usuarioRepository,
            ReservaMapper reservaMapper,
            ReservaAsientoMapper reservaAsientoMapper,
            OperacionVueloMapper operacionVueloMapper) {
        this.reservaRepository = reservaRepository;
        this.reservaAsientoRepository = reservaAsientoRepository;
        this.operacionVueloRepository = operacionVueloRepository;
        this.pasajeroRepository = pasajeroRepository;
        this.asientoAvionRepository = asientoAvionRepository;
        this.tarifaOperacionRepository = tarifaOperacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.reservaMapper = reservaMapper;
        this.reservaAsientoMapper = reservaAsientoMapper;
        this.operacionVueloMapper = operacionVueloMapper;
    }

    @Transactional
    public ReservaDetalleDto crearReserva(CreateReservaDto dto) {
        logger.info("Iniciando creacion de reserva simplificada para operacion {}", dto.operacionVueloId());

        OperacionVuelo operacionVuelo = findOperacionVueloByIdOrThrow(dto.operacionVueloId());

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + userEmail));

        BigDecimal totalReserva = BigDecimal.ZERO;
        List<ReservaAsiento> asientosReservados = new ArrayList<>();
        List<TarifaOperacion> tarifasOpsModificadas = new ArrayList<>();

        for (ItemReservaDto item : dto.items()) {
            Pasajero pasajero = findPasajeroByIdOrThrow(item.pasajeroId());
            AsientoAvion asiento = findAsientoAvionByIdOrThrow(item.asientoAvionId());
            TarifaOperacion tarifaOp = findTarifaOperacionByIdOrThrow(item.tarifaOperacionId());

            if (!asiento.getAvion().getId().equals(operacionVuelo.getAvion().getId())) {
                throw new IllegalArgumentException(
                        "El asiento " + asiento.getCodigoAsiento() + " no pertenece al avion de esta operacion.");
            }
            if (!tarifaOp.getOperacionVuelo().getId().equals(operacionVuelo.getId())) {
                throw new IllegalArgumentException("La tarifa seleccionada no pertenece a esta operacion de vuelo.");
            }
            if (!asiento.getClase().getId().equals(tarifaOp.getTarifa().getClase().getId())) {
                throw new IllegalArgumentException("La clase del asiento (" + asiento.getClase().getNombre()
                        + ") no coincide con la clase de la tarifa (" + tarifaOp.getTarifa().getClase().getNombre()
                        + ").");
            }

            if (tarifaOp.getAsientosDisponibles() <= 0) {
                throw new IllegalStateException("No quedan asientos disponibles para la tarifa "
                        + tarifaOp.getTarifa().getCodigo() + " en este vuelo.");
            }
            tarifaOp.setAsientosDisponibles(tarifaOp.getAsientosDisponibles() - 1);
            tarifasOpsModificadas.add(tarifaOp);

            ReservaAsiento ra = new ReservaAsiento();
            ra.setPasajero(pasajero);
            ra.setOperacionVuelo(operacionVuelo);
            ra.setAsientoAvion(asiento);
            ra.setTarifaOperacion(tarifaOp);
            ra.setPrecioPagado(tarifaOp.getPrecio());

            asientosReservados.add(ra);
            totalReserva = totalReserva.add(ra.getPrecioPagado());
        }

        Reserva nuevaReserva = new Reserva();
        nuevaReserva.setCodigoReserva(generarCodigoReserva());
        nuevaReserva.setUsuario(usuario);
        nuevaReserva.setFechaReserva(LocalDateTime.now());
        nuevaReserva.setEstado(EstadoReserva.ACTIVA);
        nuevaReserva.setTotal(totalReserva);
        Reserva reservaGuardada = reservaRepository.save(nuevaReserva);

        for (ReservaAsiento ra : asientosReservados) {
            ra.setReserva(reservaGuardada);
        }
        List<ReservaAsiento> asientosGuardados = reservaAsientoRepository.saveAll(asientosReservados);
        tarifaOperacionRepository.saveAll(tarifasOpsModificadas);

        ReservaDetalleDto dtoRespuesta = reservaMapper.toDto(reservaGuardada);
        List<ReservaAsientoDetalleDto> asientosDto = asientosGuardados.stream()
                .map(reservaAsientoMapper::toDto)
                .collect(Collectors.toList());
        return addAsientosToDto(dtoRespuesta, asientosDto, operacionVuelo);
    }

    @Transactional(readOnly = true)
    public ReservaDetalleDto obtenerReservaPorCodigo(String codigoReserva) {
        Reserva reserva = reservaRepository.findByCodigoReserva(codigoReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "codigoReserva", codigoReserva));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuarioAutenticado = authentication.getName();
        boolean esAdminOAgente = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + Rol.ADMIN.name())) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Rol.AGENTE.name()));

        if (!reserva.getUsuario().getCorreo().equals(correoUsuarioAutenticado) && !esAdminOAgente) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "No tiene permiso para ver esta reserva.");
        }

        List<ReservaAsiento> asientos = reservaAsientoRepository.findByReservaId(reserva.getId());
        List<ReservaAsientoDetalleDto> asientosDto = asientos.stream()
                .map(reservaAsientoMapper::toDto)
                .collect(Collectors.toList());

        OperacionVuelo operacionVuelo = asientos.isEmpty() ? null : asientos.get(0).getOperacionVuelo();
        if (operacionVuelo == null && !asientos.isEmpty()) {
            operacionVuelo = findOperacionVueloByIdOrThrow(asientos.get(0).getOperacionVuelo().getId());
        }

        ReservaDetalleDto dtoBase = reservaMapper.toDto(reserva);
        return addAsientosToDto(dtoBase, asientosDto, operacionVuelo);
    }

    @Transactional
    public ReservaDetalleDto modificarReserva(String codigoReserva, UpdateReservaDto dto) {
        if (dto == null || dto.cambios() == null || dto.cambios().isEmpty()) {
            throw new IllegalArgumentException("Debe especificar al menos un cambio para la reserva.");
        }

        Reserva reserva = reservaRepository.findByCodigoReserva(codigoReserva)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "codigoReserva", codigoReserva));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuarioAutenticado = authentication.getName();
        boolean esAdminOAgente = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_" + Rol.ADMIN.name())) ||
                authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_" + Rol.AGENTE.name()));

        if (!reserva.getUsuario().getCorreo().equals(correoUsuarioAutenticado) && !esAdminOAgente) {
            throw new org.springframework.security.access.AccessDeniedException("No tiene permiso para modificar esta reserva.");
        }

        List<ReservaAsiento> asientos = reservaAsientoRepository.findByReservaId(reserva.getId());
        var asientosPorId = asientos.stream().collect(Collectors.toMap(ReservaAsiento::getId, ra -> ra));

        Set<TarifaOperacion> tarifasPorActualizar = new HashSet<>();
        LocalDateTime ahora = LocalDateTime.now();

        for (UpdateReservaItemDto cambio : dto.cambios()) {
            ReservaAsiento reservaAsiento = asientosPorId.get(cambio.reservaAsientoId());
            if (reservaAsiento == null) {
                throw new ResourceNotFoundException("ReservaAsiento", "id", cambio.reservaAsientoId());
            }

            OperacionVuelo operacion = reservaAsiento.getOperacionVuelo();
            if (operacion.getFechaSalida().minusHours(24).isBefore(ahora)) {
                throw new IllegalStateException("No es posible modificar reservas a menos de 24 horas de la salida.");
            }

            TarifaOperacion tarifaOriginal = reservaAsiento.getTarifaOperacion();
            TarifaOperacion tarifaNueva = tarifaOriginal;

            if (cambio.tarifaOperacionId() != null && !cambio.tarifaOperacionId().equals(tarifaOriginal.getId())) {
                tarifaNueva = tarifaOperacionRepository.findById(cambio.tarifaOperacionId())
                        .orElseThrow(() -> new ResourceNotFoundException("TarifaOperacion", "id", cambio.tarifaOperacionId()));

                if (!tarifaNueva.getOperacionVuelo().getId().equals(operacion.getId())) {
                    throw new IllegalStateException("La tarifa seleccionada no pertenece a la operacion de vuelo.");
                }
                if (tarifaNueva.getAsientosDisponibles() <= 0) {
                    throw new IllegalStateException("No hay asientos disponibles para la tarifa solicitada.");
                }

                tarifaOriginal.setAsientosDisponibles(tarifaOriginal.getAsientosDisponibles() + 1);
                tarifaNueva.setAsientosDisponibles(tarifaNueva.getAsientosDisponibles() - 1);
                tarifasPorActualizar.add(tarifaOriginal);
                tarifasPorActualizar.add(tarifaNueva);

                reservaAsiento.setTarifaOperacion(tarifaNueva);
            }

            if (cambio.asientoAvionId() != null && !cambio.asientoAvionId().equals(reservaAsiento.getAsientoAvion().getId())) {
                AsientoAvion nuevoAsiento = findAsientoAvionByIdOrThrow(cambio.asientoAvionId());

                if (!nuevoAsiento.getAvion().getId().equals(operacion.getAvion().getId())) {
                    throw new IllegalStateException("El asiento seleccionado no pertenece al avion de la operacion.");
                }
                if (!nuevoAsiento.getClase().getId().equals(reservaAsiento.getTarifaOperacion().getTarifa().getClase().getId())) {
                    throw new IllegalStateException("La clase del asiento no coincide con la clase de la tarifa.");
                }
                boolean ocupado = reservaAsientoRepository.existsByOperacionVueloIdAndAsientoAvionIdAndReservaIdNot(
                        operacion.getId(), nuevoAsiento.getId(), reserva.getId());
                if (ocupado) {
                    throw new IllegalStateException("El asiento solicitado ya esta reservado para esta operacion.");
                }
                reservaAsiento.setAsientoAvion(nuevoAsiento);
            }

            if (cambio.pasajeroId() != null && !cambio.pasajeroId().equals(reservaAsiento.getPasajero().getId())) {
                Pasajero nuevoPasajero = findPasajeroByIdOrThrow(cambio.pasajeroId());
                reservaAsiento.setPasajero(nuevoPasajero);
            }

            reservaAsiento.setPrecioPagado(reservaAsiento.getTarifaOperacion().getPrecio());
        }

        if (!tarifasPorActualizar.isEmpty()) {
            tarifaOperacionRepository.saveAll(tarifasPorActualizar);
        }
        reservaAsientoRepository.saveAll(asientos);

        BigDecimal nuevoTotal = asientos.stream()
                .map(ReservaAsiento::getPrecioPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        reserva.setTotal(nuevoTotal);
        reservaRepository.save(reserva);

        List<ReservaAsientoDetalleDto> asientosDto = asientos.stream()
                .map(reservaAsientoMapper::toDto)
                .collect(Collectors.toList());
        OperacionVueloDto operacionDto = operacionVueloMapper.toDto(asientos.get(0).getOperacionVuelo());

        ReservaDetalleDto dtoBase = reservaMapper.toDto(reserva);
        return new ReservaDetalleDto(
                dtoBase.id(),
                dtoBase.codigoReserva(),
                dtoBase.usuario(),
                dtoBase.fechaReserva(),
                dtoBase.estado(),
                reserva.getTotal(),
                operacionDto,
                asientosDto);
    }

    @Transactional(readOnly = true)
    public Page<ReservaAdminDto> buscarReservasAdmin(Pageable pageable,
            Optional<EstadoReserva> estado,
            Optional<LocalDate> fechaDesde,
            Optional<LocalDate> fechaHasta,
            Optional<Long> aerolineaId,
            Optional<Long> origenId,
            Optional<Long> destinoId) {

        Specification<Reserva> spec = null;
        spec = combine(spec, ReservaSpecification.hasEstado(estado.orElse(null)));
        spec = combine(spec, ReservaSpecification.fechaDesde(fechaDesde.map(LocalDate::atStartOfDay).orElse(null)));
        spec = combine(spec, ReservaSpecification.fechaHasta(fechaHasta.map(d -> d.atTime(23, 59, 59)).orElse(null)));
        spec = combine(spec, ReservaSpecification.hasAerolinea(aerolineaId.orElse(null)));
        spec = combine(spec, ReservaSpecification.hasOrigen(origenId.orElse(null)));
        spec = combine(spec, ReservaSpecification.hasDestino(destinoId.orElse(null)));

        Page<Reserva> pagina = spec == null ? reservaRepository.findAll(pageable) : reservaRepository.findAll(spec, pageable);
        return pagina.map(this::mapToAdminDto);
    }

    @Transactional(readOnly = true)
    public List<ReservaDetalleDto> obtenerMisReservas() {
        String correoUsuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuarioAutenticado)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con correo: " + correoUsuarioAutenticado));

        List<Reserva> reservas = reservaRepository.findByUsuarioId(usuario.getId());

        return reservas.stream().map(reserva -> {
            List<ReservaAsiento> asientos = reservaAsientoRepository.findByReservaId(reserva.getId());
            List<ReservaAsientoDetalleDto> asientosDto = asientos.stream()
                    .map(reservaAsientoMapper::toDto)
                    .collect(Collectors.toList());
            OperacionVuelo operacionVuelo = asientos.isEmpty() ? null
                    : findOperacionVueloByIdOrThrow(asientos.get(0).getOperacionVuelo().getId());

            ReservaDetalleDto dtoBase = reservaMapper.toDto(reserva);
            return addAsientosToDto(dtoBase, asientosDto, operacionVuelo);
        }).collect(Collectors.toList());
    }

    private String generarCodigoReserva() {
        return UUID.randomUUID().toString().toUpperCase().substring(0, 6);
    }

    private OperacionVuelo findOperacionVueloByIdOrThrow(Long id) {
        return operacionVueloRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OperacionVuelo", "id", id));
    }

    private Pasajero findPasajeroByIdOrThrow(Long id) {
        return pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", id));
    }

    private AsientoAvion findAsientoAvionByIdOrThrow(Long id) {
        return asientoAvionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("AsientoAvion", "id", id));
    }

    private TarifaOperacion findTarifaOperacionByIdOrThrow(Long id) {
        return tarifaOperacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TarifaOperacion", "id", id));
    }

    private ReservaDetalleDto addAsientosToDto(ReservaDetalleDto baseDto, List<ReservaAsientoDetalleDto> asientos,
            OperacionVuelo opVuelo) {
        OperacionVueloDto opDto = operacionVueloMapper.toDto(opVuelo);
        return new ReservaDetalleDto(
                baseDto.id(), baseDto.codigoReserva(), baseDto.usuario(),
                baseDto.fechaReserva(), baseDto.estado(), baseDto.total(),
                opDto,
                asientos);
    }

    private ReservaAdminDto mapToAdminDto(Reserva reserva) {
        List<ReservaAsiento> asientos = reservaAsientoRepository.findByReservaId(reserva.getId());
        String aerolinea = null;
        String origen = null;
        String destino = null;

        if (!asientos.isEmpty()) {
            OperacionVuelo operacion = asientos.get(0).getOperacionVuelo();
            Vuelo vuelo = operacion.getVuelo();
            aerolinea = vuelo.getAerolinea().getNombre();
            origen = vuelo.getRuta().getOrigen().getNombre();
            destino = vuelo.getRuta().getDestino().getNombre();
        }

        Usuario usuario = reserva.getUsuario();
        return new ReservaAdminDto(
                reserva.getId(),
                reserva.getCodigoReserva(),
                reserva.getEstado(),
                reserva.getFechaReserva(),
                reserva.getTotal(),
                usuario.getNombre(),
                usuario.getCorreo(),
                aerolinea,
                origen,
                destino);
    }

    private Specification<Reserva> combine(Specification<Reserva> base, Specification<Reserva> other) {
        if (other == null) {
            return base;
        }
        return base == null ? Specification.where(other) : base.and(other);
    }
}

