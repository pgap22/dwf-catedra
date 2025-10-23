package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Pasajero;
import com.udb.aerovia_api.dto.CreatePasajeroDto;
import com.udb.aerovia_api.dto.PasajeroDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.PasajeroMapper;
import com.udb.aerovia_api.repository.PasajeroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasajeroService {

    private final PasajeroRepository pasajeroRepository;
    private final PasajeroMapper pasajeroMapper;

    public PasajeroService(PasajeroRepository pasajeroRepository, PasajeroMapper pasajeroMapper) {
        this.pasajeroRepository = pasajeroRepository;
        this.pasajeroMapper = pasajeroMapper;
    }

    @Transactional
    public PasajeroDto crearPasajero(CreatePasajeroDto createDto) {
        // La validación de pasaporte duplicado se maneja a nivel de DB con la constraint
        Pasajero nuevoPasajero = pasajeroMapper.toEntity(createDto);
        return pasajeroMapper.toDto(pasajeroRepository.save(nuevoPasajero));
    }

    @Transactional(readOnly = true)
    public List<PasajeroDto> obtenerTodos() {
        return pasajeroRepository.findAll().stream()
                .map(pasajeroMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PasajeroDto obtenerPorId(Long id) {
        return pasajeroRepository.findById(id)
                .map(pasajeroMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", id));
    }

    @Transactional
    public PasajeroDto actualizarPasajero(Long id, CreatePasajeroDto updateDto) {
        Pasajero pasajeroExistente = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", id));

        pasajeroExistente.setNombreCompleto(updateDto.nombreCompleto());
        pasajeroExistente.setFechaNacimiento(updateDto.fechaNacimiento());
        pasajeroExistente.setNroPasaporte(updateDto.nroPasaporte());

        return pasajeroMapper.toDto(pasajeroRepository.save(pasajeroExistente));
    }

    @Transactional
    public void eliminarPasajero(Long id) {
        if (!pasajeroRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pasajero", "id", id);
        }
        pasajeroRepository.deleteById(id);
    }
}