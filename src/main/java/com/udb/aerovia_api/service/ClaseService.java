package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Clase;
import com.udb.aerovia_api.dto.ClaseDto;
import com.udb.aerovia_api.dto.CreateClaseDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.ClaseMapper;
import com.udb.aerovia_api.repository.ClaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ClaseMapper claseMapper;

    public ClaseService(ClaseRepository claseRepository, ClaseMapper claseMapper) {
        this.claseRepository = claseRepository;
        this.claseMapper = claseMapper;
    }

    @Transactional
    public ClaseDto crearClase(CreateClaseDto createDto) {
        Clase nuevaClase = claseMapper.toEntity(createDto);
        return claseMapper.toDto(claseRepository.save(nuevaClase));
    }

    @Transactional(readOnly = true)
    public List<ClaseDto> obtenerTodas() {
        return claseRepository.findAll().stream()
                .map(claseMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClaseDto obtenerPorId(Long id) {
        return claseRepository.findById(id)
                .map(claseMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", id));
    }

    @Transactional
    public ClaseDto actualizarClase(Long id, CreateClaseDto updateDto) {
        Clase claseExistente = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", id));

        claseExistente.setNombre(updateDto.nombre());
        claseExistente.setDescripcion(updateDto.descripcion());

        return claseMapper.toDto(claseRepository.save(claseExistente));
    }

    @Transactional
    public void eliminarClase(Long id) {
        if (!claseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Clase", "id", id);
        }
        claseRepository.deleteById(id);
    }
}