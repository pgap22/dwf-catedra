package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.OperacionTripulacion;
import com.udb.aerovia_api.domain.OperacionVuelo;
import com.udb.aerovia_api.domain.Tripulante;
import com.udb.aerovia_api.dto.AsignarTripulanteDto;
import com.udb.aerovia_api.dto.OperacionTripulacionDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.TripulatesOperacionMapper;
import com.udb.aerovia_api.repository.OperacionTripulacionRepository;
import com.udb.aerovia_api.repository.OperacionVueloRepository;
import com.udb.aerovia_api.repository.TripulanteRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OperacionTripulacionService {

    private final OperacionTripulacionRepository operacionTripulacionRepository;
    private final OperacionVueloRepository operacionVueloRepository;
    private final TripulanteRepository tripulanteRepository;
    private final TripulatesOperacionMapper tripulatesOperacionMapper;

    public OperacionTripulacionService(
            OperacionTripulacionRepository operacionTripulacionRepository,
            TripulatesOperacionMapper tripulatesOperacionMapper,
            OperacionVueloRepository operacionVueloRepository,
            TripulanteRepository tripulanteRepository) {

        this.operacionTripulacionRepository = operacionTripulacionRepository;
        this.operacionVueloRepository = operacionVueloRepository;
        this.tripulanteRepository = tripulanteRepository;
        this.tripulatesOperacionMapper = tripulatesOperacionMapper;
    }

    @Transactional
    public OperacionTripulacionDto asignarTripulante(AsignarTripulanteDto dto) {
        OperacionVuelo operacion = operacionVueloRepository.findById(dto.operacionId())
                .orElseThrow(() -> new ResourceNotFoundException("OperacionVuelo", "id", dto.operacionId()));

        Tripulante tripulante = tripulanteRepository.findById(dto.tripulanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Tripulante", "id", dto.tripulanteId()));

        if (operacionTripulacionRepository.existsByOperacionVueloIdAndTripulanteId(dto.operacionId(), dto.tripulanteId())) {
            throw new DuplicateResourceException("El tripulante ya esta asignado a la operacion.");
        }

        OperacionTripulacion asignacion = new OperacionTripulacion();
        asignacion.setOperacionVuelo(operacion);
        asignacion.setTripulante(tripulante);
        asignacion.setRolEnVuelo(dto.rolEnVuelo());

        return tripulatesOperacionMapper.toDto(operacionTripulacionRepository.save(asignacion));
    }

    @Transactional
    public void desasignarTripulante(Long asignacionId) {
        if (!operacionTripulacionRepository.existsById(asignacionId)) {
            throw new ResourceNotFoundException("Asignacion de Tripulante", "id", asignacionId);
        }
        operacionTripulacionRepository.deleteById(asignacionId);
    }

    @Transactional(readOnly = true)
    public List<OperacionTripulacionDto> obtenerTripulacionPorOperacion(Long operacionId) {
        operacionVueloRepository.findById(operacionId)
                .orElseThrow(() -> new ResourceNotFoundException("OperacionVuelo", "id", operacionId));

        return operacionTripulacionRepository.findByOperacionVueloId(operacionId).stream()
                .map(tripulatesOperacionMapper::toDto)
                .collect(Collectors.toList());
    }
}
