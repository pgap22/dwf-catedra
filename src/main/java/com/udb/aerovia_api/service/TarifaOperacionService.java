package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.OperacionVuelo;
import com.udb.aerovia_api.domain.Tarifa;
import com.udb.aerovia_api.domain.TarifaOperacion;
import com.udb.aerovia_api.dto.CreateTarifaOperacionDto;
import com.udb.aerovia_api.dto.TarifaOperacionDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.TarifaOperacionMapper;
import com.udb.aerovia_api.repository.OperacionVueloRepository;
import com.udb.aerovia_api.repository.TarifaOperacionRepository;
import com.udb.aerovia_api.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarifaOperacionService {

    private final TarifaOperacionRepository tarifaOperacionRepository;
    private final OperacionVueloRepository operacionVueloRepository;
    private final TarifaRepository tarifaRepository;
    private final TarifaOperacionMapper mapper;

    public TarifaOperacionService(TarifaOperacionRepository tarifaOperacionRepository, OperacionVueloRepository operacionVueloRepository, TarifaRepository tarifaRepository, TarifaOperacionMapper mapper) {
        this.tarifaOperacionRepository = tarifaOperacionRepository;
        this.operacionVueloRepository = operacionVueloRepository;
        this.tarifaRepository = tarifaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public TarifaOperacionDto asignarTarifaAOperacion(CreateTarifaOperacionDto dto) {
        // Verificar si ya existe esta combinación (constraint uq_tarifa_por_operacion)
         tarifaOperacionRepository.findByOperacionVueloIdAndTarifaId(dto.operacionId(), dto.tarifaId()).ifPresent(to -> {
             throw new DuplicateResourceException("Esta tarifa ya ha sido asignada a esta operación de vuelo.");
         });

        OperacionVuelo operacion = operacionVueloRepository.findById(dto.operacionId())
            .orElseThrow(() -> new ResourceNotFoundException("OperacionVuelo", "id", dto.operacionId()));

        Tarifa tarifa = tarifaRepository.findById(dto.tarifaId())
            .orElseThrow(() -> new ResourceNotFoundException("Tarifa", "id", dto.tarifaId()));

        TarifaOperacion nuevaAsignacion = new TarifaOperacion();
        nuevaAsignacion.setOperacionVuelo(operacion);
        nuevaAsignacion.setTarifa(tarifa);
        nuevaAsignacion.setPrecio(dto.precio());
        nuevaAsignacion.setAsientosDisponibles(dto.asientosDisponibles());

        return mapper.toDto(tarifaOperacionRepository.save(nuevaAsignacion));
    }

    @Transactional(readOnly = true)
    public List<TarifaOperacionDto> obtenerTarifasPorOperacion(Long operacionId) {
        if (!operacionVueloRepository.existsById(operacionId)) {
            throw new ResourceNotFoundException("OperacionVuelo", "id", operacionId);
        }
        return tarifaOperacionRepository.findByOperacionVueloId(operacionId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TarifaOperacionDto obtenerTarifaOperacionPorId(Long id) {
        return tarifaOperacionRepository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("TarifaOperacion", "id", id));
    }

    @Transactional
    public TarifaOperacionDto actualizarTarifaOperacion(Long id, CreateTarifaOperacionDto dto) {
         TarifaOperacion existente = tarifaOperacionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("TarifaOperacion", "id", id));

         // Validar que no se cambie la operación o tarifa, solo precio/asientos
         if (!existente.getOperacionVuelo().getId().equals(dto.operacionId()) || !existente.getTarifa().getId().equals(dto.tarifaId())) {
             throw new IllegalArgumentException("No se puede cambiar la operación o la tarifa de una asignación existente.");
         }

         existente.setPrecio(dto.precio());
         existente.setAsientosDisponibles(dto.asientosDisponibles());

         return mapper.toDto(tarifaOperacionRepository.save(existente));
    }

    @Transactional
    public void eliminarTarifaDeOperacion(Long id) {
         if (!tarifaOperacionRepository.existsById(id)) {
             throw new ResourceNotFoundException("TarifaOperacion", "id", id);
         }
         tarifaOperacionRepository.deleteById(id);
    }
}