package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Avion;
import com.udb.aerovia_api.dto.AvionDto;
import com.udb.aerovia_api.dto.CreateAvionDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.AvionMapper;
import com.udb.aerovia_api.repository.AvionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvionService {

    private final AvionRepository avionRepository;
    private final AvionMapper avionMapper;

    public AvionService(AvionRepository avionRepository, AvionMapper avionMapper) {
        this.avionRepository = avionRepository;
        this.avionMapper = avionMapper;
    }

    @Transactional
    public AvionDto crearAvion(CreateAvionDto createDto) {
        avionRepository.findByMatricula(createDto.matricula()).ifPresent(a -> {
            throw new DuplicateResourceException("La matrícula '" + createDto.matricula() + "' ya existe.");
        });
        Avion nuevoAvion = avionMapper.toEntity(createDto);
        return avionMapper.toDto(avionRepository.save(nuevoAvion));
    }

    @Transactional(readOnly = true)
    public List<AvionDto> obtenerTodos() {
        return avionRepository.findAll().stream()
                .map(avionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AvionDto obtenerPorId(Long id) {
        return avionRepository.findById(id)
                .map(avionMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Avion", "id", id));
    }

    @Transactional
    public AvionDto actualizarAvion(Long id, CreateAvionDto updateDto) {
        Avion avionExistente = avionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Avion", "id", id));

        avionRepository.findByMatricula(updateDto.matricula()).ifPresent(a -> {
            if (!a.getId().equals(id)) {
                throw new DuplicateResourceException("La matrícula '" + updateDto.matricula() + "' ya está en uso.");
            }
        });

        avionExistente.setMatricula(updateDto.matricula());
        avionExistente.setModelo(updateDto.modelo());
        avionExistente.setCapacidadTotal(updateDto.capacidadTotal());

        return avionMapper.toDto(avionRepository.save(avionExistente));
    }

    @Transactional
    public void eliminarAvion(Long id) {
        if (!avionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Avion", "id", id);
        }
        avionRepository.deleteById(id);
    }
}