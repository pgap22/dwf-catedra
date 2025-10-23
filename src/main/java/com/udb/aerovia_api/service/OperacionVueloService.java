package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Avion;
import com.udb.aerovia_api.domain.OperacionVuelo;
import com.udb.aerovia_api.domain.Vuelo;
import com.udb.aerovia_api.dto.BusquedaVueloParamsDto;
import com.udb.aerovia_api.dto.CreateOperacionVueloDto;
import com.udb.aerovia_api.dto.OperacionVueloDto;
import com.udb.aerovia_api.dto.TarifaOperacionDto;
import com.udb.aerovia_api.dto.VueloDisponibleDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.AerolineaMapper;
import com.udb.aerovia_api.mapper.AeropuertoMapper;
import com.udb.aerovia_api.mapper.AvionMapper;
import com.udb.aerovia_api.mapper.OperacionVueloMapper;
import com.udb.aerovia_api.mapper.TarifaOperacionMapper;
import com.udb.aerovia_api.repository.AvionRepository;
import com.udb.aerovia_api.repository.OperacionVueloRepository;
import com.udb.aerovia_api.repository.TarifaOperacionRepository;
import com.udb.aerovia_api.repository.VueloRepository;
import com.udb.aerovia_api.repository.specification.OperacionVueloSpecification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OperacionVueloService {

    private final OperacionVueloRepository operacionVueloRepository;
    private final VueloRepository vueloRepository;
    private final AvionRepository avionRepository;
    private final OperacionVueloMapper operacionVueloMapper;
    private final TarifaOperacionRepository tarifaOperacionRepository;
    private final TarifaOperacionMapper tarifaOperacionMapper;
    private final AerolineaMapper aerolineaMapper;
    private final AeropuertoMapper aeropuertoMapper;
    private final AvionMapper avionMapper;

    public OperacionVueloService(OperacionVueloRepository operacionVueloRepository,
            VueloRepository vueloRepository,
            AvionRepository avionRepository,
            OperacionVueloMapper operacionVueloMapper,
            TarifaOperacionRepository tarifaOperacionRepository,
            TarifaOperacionMapper tarifaOperacionMapper,
            AerolineaMapper aerolineaMapper,
            AeropuertoMapper aeropuertoMapper,
            AvionMapper avionMapper) {
        this.operacionVueloRepository = operacionVueloRepository;
        this.vueloRepository = vueloRepository;
        this.avionRepository = avionRepository;
        this.operacionVueloMapper = operacionVueloMapper;
        this.tarifaOperacionRepository = tarifaOperacionRepository;
        this.tarifaOperacionMapper = tarifaOperacionMapper;
        this.aerolineaMapper = aerolineaMapper;
        this.aeropuertoMapper = aeropuertoMapper;
        this.avionMapper = avionMapper;
    }

    @Transactional
    public OperacionVueloDto programarVuelo(CreateOperacionVueloDto createDto) {
        if (createDto.fechaLlegada().isBefore(createDto.fechaSalida())
                || createDto.fechaLlegada().isEqual(createDto.fechaSalida())) {
            throw new IllegalArgumentException("La fecha de llegada debe ser posterior a la fecha de salida.");
        }

        Vuelo vuelo = vueloRepository.findById(createDto.vueloId())
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo", "id", createDto.vueloId()));

        Avion avion = avionRepository.findById(createDto.avionId())
                .orElseThrow(() -> new ResourceNotFoundException("Avion", "id", createDto.avionId()));

        OperacionVuelo nuevaOperacion = new OperacionVuelo();
        nuevaOperacion.setVuelo(vuelo);
        nuevaOperacion.setAvion(avion);
        nuevaOperacion.setFechaSalida(createDto.fechaSalida());
        nuevaOperacion.setFechaLlegada(createDto.fechaLlegada());
        nuevaOperacion.setEstado(createDto.estado());

        return operacionVueloMapper.toDto(operacionVueloRepository.save(nuevaOperacion));
    }

    // Nuevo método de búsqueda
    @Transactional(readOnly = true)
    public List<VueloDisponibleDto> buscarVuelosDisponibles(BusquedaVueloParamsDto params) {
        Specification<OperacionVuelo> spec = OperacionVueloSpecification.buscarPorCriterios(
                params.origenId(), params.destinoId(), params.fechaSalida());

        List<OperacionVuelo> operaciones = operacionVueloRepository.findAll(spec);

        return operaciones.stream()
                .map(this::convertirA_VueloDisponibleDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OperacionVueloDto> obtenerTodas() {
        return operacionVueloRepository.findAll().stream()
                .map(operacionVueloMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OperacionVueloDto obtenerPorId(Long id) {
        return operacionVueloRepository.findById(id)
                .map(operacionVueloMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("OperacionVuelo", "id", id));
    }

    @Transactional
    public void eliminarOperacion(Long id) {
        if (!operacionVueloRepository.existsById(id)) {
            throw new ResourceNotFoundException("OperacionVuelo", "id", id);
        }
        // Aquí iría lógica de negocio compleja, como notificar a pasajeros,
        // antes de simplemente borrar. Por ahora, es un borrado simple.
        operacionVueloRepository.deleteById(id);
    }

    // Método helper para la conversión (ya lo teníamos)
    private VueloDisponibleDto convertirA_VueloDisponibleDto(OperacionVuelo op) {
        List<TarifaOperacionDto> tarifas = tarifaOperacionRepository.findByOperacionVueloId(op.getId())
                .stream()
                // Filtrar tarifas que aún tengan asientos disponibles
                .filter(to -> to.getAsientosDisponibles() > 0)
                .map(tarifaOperacionMapper::toDto)
                .collect(Collectors.toList());

        // Si no hay tarifas con asientos, no mostramos este vuelo
        if (tarifas.isEmpty()) {
            return null; // O podríamos manejarlo de otra forma
        }

        return new VueloDisponibleDto(
                op.getId(),
                op.getVuelo().getNumeroVuelo(),
                aerolineaMapper.toDto(op.getVuelo().getAerolinea()),
                aeropuertoMapper.toDto(op.getVuelo().getRuta().getOrigen()),
                aeropuertoMapper.toDto(op.getVuelo().getRuta().getDestino()),
                op.getFechaSalida(),
                op.getFechaLlegada(),
                op.getVuelo().getDuracionMin(),
                avionMapper.toDto(op.getAvion()),
                tarifas);
    }
}