package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Aeropuerto;
import com.udb.aerovia_api.dto.AeropuertoDto;
import com.udb.aerovia_api.dto.CreateAeropuertoDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.AeropuertoMapper;
import com.udb.aerovia_api.repository.AeropuertoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AeropuertoService {

    private final AeropuertoRepository aeropuertoRepository;
    private final AeropuertoMapper aeropuertoMapper;

    public AeropuertoService(AeropuertoRepository aeropuertoRepository, AeropuertoMapper aeropuertoMapper) {
        this.aeropuertoRepository = aeropuertoRepository;
        this.aeropuertoMapper = aeropuertoMapper;
    }

    @Transactional
    public AeropuertoDto crearAeropuerto(CreateAeropuertoDto createDto) {
        aeropuertoRepository.findByCodigoIata(createDto.codigoIata()).ifPresent(a -> {
            throw new DuplicateResourceException("El código IATA '" + createDto.codigoIata() + "' ya existe.");
        });
        Aeropuerto nuevoAeropuerto = aeropuertoMapper.toEntity(createDto);
        return aeropuertoMapper.toDto(aeropuertoRepository.save(nuevoAeropuerto));
    }

    @Transactional(readOnly = true)
    public List<AeropuertoDto> obtenerTodos() {
        return aeropuertoRepository.findAll().stream()
                .map(aeropuertoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AeropuertoDto obtenerPorId(Long id) {
        return aeropuertoRepository.findById(id)
                .map(aeropuertoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto", "id", id));
    }

    @Transactional
    public AeropuertoDto actualizarAeropuerto(Long id, CreateAeropuertoDto updateDto) {
        Aeropuerto aeropuertoExistente = aeropuertoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto", "id", id));

        aeropuertoRepository.findByCodigoIata(updateDto.codigoIata()).ifPresent(a -> {
            if (!a.getId().equals(id)) {
                throw new DuplicateResourceException("El código IATA '" + updateDto.codigoIata() + "' ya está en uso.");
            }
        });

        aeropuertoExistente.setCodigoIata(updateDto.codigoIata());
        aeropuertoExistente.setNombre(updateDto.nombre());
        aeropuertoExistente.setCiudad(updateDto.ciudad());
        aeropuertoExistente.setPais(updateDto.pais());

        return aeropuertoMapper.toDto(aeropuertoRepository.save(aeropuertoExistente));
    }

    @Transactional
    public void eliminarAeropuerto(Long id) {
        if (!aeropuertoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aeropuerto", "id", id);
        }
        aeropuertoRepository.deleteById(id);
    }
}