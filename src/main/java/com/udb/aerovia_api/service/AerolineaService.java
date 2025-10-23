package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Aerolinea;
import com.udb.aerovia_api.dto.AerolineaDto;
import com.udb.aerovia_api.dto.CreateAerolineaDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.AerolineaMapper;
import com.udb.aerovia_api.repository.AerolineaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AerolineaService {

    private final AerolineaRepository aerolineaRepository;
    private final AerolineaMapper aerolineaMapper; // 1. Inyectamos el Mapper

    public AerolineaService(AerolineaRepository aerolineaRepository, AerolineaMapper aerolineaMapper) {
        this.aerolineaRepository = aerolineaRepository;
        this.aerolineaMapper = aerolineaMapper;
    }

    @Transactional(readOnly = true)
    public List<AerolineaDto> obtenerTodasLasAerolineas() {
        return aerolineaRepository.findAll().stream()
                .map(aerolineaMapper::toDto) // 3. Usamos el mapper
                .collect(Collectors.toList());
    }

    @Transactional
    public AerolineaDto crearAerolinea(CreateAerolineaDto createDto) {
        // 4. Usamos el mapper para convertir DTO a Entidad
        var nuevaAerolinea = aerolineaMapper.toEntity(createDto);

        var aerolineaGuardada = aerolineaRepository.save(nuevaAerolinea);

        // 5. Y para convertir Entidad a DTO en el retorno
        return aerolineaMapper.toDto(aerolineaGuardada);
    }

    @Transactional(readOnly = true)
    public AerolineaDto obtenerAerolineaPorId(Long id) {
        return aerolineaRepository.findById(id)
                .map(aerolineaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Aerolinea", "id", id));
    }

    @Transactional
    public AerolineaDto actualizarAerolinea(Long id, CreateAerolineaDto updateDto) {
        // Primero, buscamos la aerolínea existente
        Aerolinea aerolineaExistente = aerolineaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aerolinea", "id", id));

        // Verificamos si el nuevo código IATA ya está en uso por otra aerolínea
        aerolineaRepository.findByCodigoIata(updateDto.codigoIata()).ifPresent(aerolinea -> {
            if (!aerolinea.getId().equals(id)) {
                throw new DuplicateResourceException("El código IATA '" + updateDto.codigoIata() + "' ya está en uso.");
            }
        });

        // Actualizamos los campos
        aerolineaExistente.setNombre(updateDto.nombre());
        aerolineaExistente.setCodigoIata(updateDto.codigoIata());

        // Guardamos los cambios
        Aerolinea aerolineaActualizada = aerolineaRepository.save(aerolineaExistente);

        return aerolineaMapper.toDto(aerolineaActualizada);
    }

    @Transactional
    public void eliminarAerolinea(Long id) {
        // Verificamos que la aerolínea exista antes de intentar borrarla
        if (!aerolineaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aerolinea", "id", id);
        }
        aerolineaRepository.deleteById(id);
    }

}