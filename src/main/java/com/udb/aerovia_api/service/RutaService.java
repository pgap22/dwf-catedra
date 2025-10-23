package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Aeropuerto;
import com.udb.aerovia_api.domain.Ruta;
import com.udb.aerovia_api.dto.CreateRutaDto;
import com.udb.aerovia_api.dto.RutaDto;
import com.udb.aerovia_api.dto.UpdateRutaDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.RutaMapper;
import com.udb.aerovia_api.repository.AeropuertoRepository;
import com.udb.aerovia_api.repository.RutaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RutaService {

    private final RutaRepository rutaRepository;
    private final AeropuertoRepository aeropuertoRepository;
    private final RutaMapper rutaMapper;

    public RutaService(RutaRepository rutaRepository, AeropuertoRepository aeropuertoRepository, RutaMapper rutaMapper) {
        this.rutaRepository = rutaRepository;
        this.aeropuertoRepository = aeropuertoRepository;
        this.rutaMapper = rutaMapper;
    }

    @Transactional
    public RutaDto crearRuta(CreateRutaDto createDto) {
        if (Objects.equals(createDto.origenId(), createDto.destinoId())) {
            throw new IllegalArgumentException("El aeropuerto de origen y destino no pueden ser el mismo.");
        }

        Aeropuerto origen = aeropuertoRepository.findById(createDto.origenId())
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de origen", "id", createDto.origenId()));

        Aeropuerto destino = aeropuertoRepository.findById(createDto.destinoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de destino", "id", createDto.destinoId()));

        Ruta nuevaRuta = new Ruta();
        nuevaRuta.setOrigen(origen);
        nuevaRuta.setDestino(destino);
        nuevaRuta.setDistanciaKm(createDto.distanciaKm());

        return rutaMapper.toDto(rutaRepository.save(nuevaRuta));
    }

    @Transactional(readOnly = true)
    public List<RutaDto> obtenerTodas() {
        return rutaRepository.findAll().stream()
                .map(rutaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RutaDto obtenerPorId(Long id) {
        return rutaRepository.findById(id)
                .map(rutaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", "id", id));
    }

    @Transactional
    public void eliminarRuta(Long id) {
        if (!rutaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Ruta", "id", id);
        }
        rutaRepository.deleteById(id);
    }

    @Transactional
    public RutaDto actualizarRuta(Long id, UpdateRutaDto updateDto) {
        Ruta ruta = rutaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", "id", id));

        Long origenIdFinal = updateDto.origenId() != null ? updateDto.origenId() : ruta.getOrigen().getId();
        Long destinoIdFinal = updateDto.destinoId() != null ? updateDto.destinoId() : ruta.getDestino().getId();

        if (Objects.equals(origenIdFinal, destinoIdFinal)) {
            throw new IllegalArgumentException("El aeropuerto de origen y destino no pueden ser el mismo.");
        }

        if (updateDto.origenId() != null && !Objects.equals(updateDto.origenId(), ruta.getOrigen().getId())) {
            Aeropuerto nuevoOrigen = aeropuertoRepository.findById(updateDto.origenId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de origen", "id", updateDto.origenId()));
            ruta.setOrigen(nuevoOrigen);
        }

        if (updateDto.destinoId() != null && !Objects.equals(updateDto.destinoId(), ruta.getDestino().getId())) {
            Aeropuerto nuevoDestino = aeropuertoRepository.findById(updateDto.destinoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Aeropuerto de destino", "id", updateDto.destinoId()));
            ruta.setDestino(nuevoDestino);
        }

        if (updateDto.distanciaKm() != null) {
            ruta.setDistanciaKm(updateDto.distanciaKm());
        }

        return rutaMapper.toDto(rutaRepository.save(ruta));
    }
}
