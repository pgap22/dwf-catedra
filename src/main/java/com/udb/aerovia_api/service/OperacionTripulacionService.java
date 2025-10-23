package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.OperacionTripulacion;
import com.udb.aerovia_api.domain.OperacionVuelo;
import com.udb.aerovia_api.domain.Tripulante;
import com.udb.aerovia_api.dto.AsignarTripulanteDto;
import com.udb.aerovia_api.dto.OperacionTripulacionDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.OperacionTripulacionMapper;
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
    private final OperacionTripulacionMapper mapper;
    private final TripulatesOperacionMapper tripulatesOperacionMapper;

    public OperacionTripulacionService(
            OperacionTripulacionRepository operacionTripulacionRepository,
            TripulatesOperacionMapper tripulatesOperacionMapper,
            OperacionVueloRepository operacionVueloRepository,
            OperacionTripulacionMapper mapper,
            TripulanteRepository tripulanteRepository) {

        this.operacionTripulacionRepository = operacionTripulacionRepository;
        this.operacionVueloRepository = operacionVueloRepository;
        this.tripulanteRepository = tripulanteRepository;
        this.mapper = mapper;
        this.tripulatesOperacionMapper = tripulatesOperacionMapper;
    }

    @Transactional
    public OperacionTripulacionDto asignarTripulante(AsignarTripulanteDto dto) {
        OperacionVuelo operacion = operacionVueloRepository.findById(dto.operacionId())
                .orElseThrow(() -> new ResourceNotFoundException("OperacionVuelo", "id", dto.operacionId()));

        Tripulante tripulante = tripulanteRepository.findById(dto.tripulanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Tripulante", "id", dto.tripulanteId()));

        OperacionTripulacion asignacion = new OperacionTripulacion();
        asignacion.setOperacionVuelo(operacion);
        asignacion.setTripulante(tripulante);
        asignacion.setRolEnVuelo(dto.rolEnVuelo());

        return mapper.toDto(operacionTripulacionRepository.save(asignacion));
    }

    @Transactional
    public void desasignarTripulante(Long asignacionId) {
        if (!operacionTripulacionRepository.existsById(asignacionId)) {
            throw new ResourceNotFoundException("Asignacion de Tripulante", "id", asignacionId);
        }
        operacionTripulacionRepository.deleteById(asignacionId);
    }

    @Transactional(readOnly = true)
    public List<OperacionTripulacionDto> listarTripulatesByOperacionId(Long operacionVuelo) {
        List<OperacionTripulacion> tripulacions = operacionTripulacionRepository
                .findByOperacionVueloId(operacionVuelo);

        List<OperacionTripulacionDto> dtos = tripulacions.stream().map(tripulatesOperacionMapper::toDto)
                .collect(Collectors.toList());

        return dtos;
    }
}