package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Tripulante;
import com.udb.aerovia_api.dto.CreateTripulanteDto;
import com.udb.aerovia_api.dto.TripulanteDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.TripulanteMapper;
import com.udb.aerovia_api.repository.TripulanteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripulanteService {

    private final TripulanteRepository tripulanteRepository;
    private final TripulanteMapper tripulanteMapper;

    public TripulanteService(TripulanteRepository tripulanteRepository, TripulanteMapper tripulanteMapper) {
        this.tripulanteRepository = tripulanteRepository;
        this.tripulanteMapper = tripulanteMapper;
    }

    @Transactional
    public TripulanteDto crearTripulante(CreateTripulanteDto createDto) {
        Tripulante nuevoTripulante = tripulanteMapper.toEntity(createDto);
        return tripulanteMapper.toDto(tripulanteRepository.save(nuevoTripulante));
    }

    @Transactional(readOnly = true)
    public List<TripulanteDto> obtenerTodos() {
        return tripulanteRepository.findAll().stream()
                .map(tripulanteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TripulanteDto obtenerPorId(Long id) {
        return tripulanteRepository.findById(id)
                .map(tripulanteMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tripulante", "id", id));
    }

    @Transactional
    public TripulanteDto actualizarTripulante(Long id, CreateTripulanteDto updateDto) {
        Tripulante tripulanteExistente = tripulanteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tripulante", "id", id));

        tripulanteExistente.setNombre(updateDto.nombre());
        tripulanteExistente.setTipo(updateDto.tipo());

        return tripulanteMapper.toDto(tripulanteRepository.save(tripulanteExistente));
    }

    @Transactional
    public void eliminarTripulante(Long id) {
        if (!tripulanteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Tripulante", "id", id);
        }
        tripulanteRepository.deleteById(id);
    }
}