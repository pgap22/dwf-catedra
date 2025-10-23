package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Aerolinea;
import com.udb.aerovia_api.domain.Ruta;
import com.udb.aerovia_api.domain.Vuelo;
import com.udb.aerovia_api.dto.CreateVueloDto;
import com.udb.aerovia_api.dto.VueloDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.VueloMapper;
import com.udb.aerovia_api.repository.AerolineaRepository;
import com.udb.aerovia_api.repository.RutaRepository;
import com.udb.aerovia_api.repository.VueloRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class VueloService {

    private final VueloRepository vueloRepository;
    private final AerolineaRepository aerolineaRepository;
    private final RutaRepository rutaRepository;
    private final VueloMapper vueloMapper;

    public VueloService(VueloRepository vueloRepository, AerolineaRepository aerolineaRepository, RutaRepository rutaRepository, VueloMapper vueloMapper) {
        this.vueloRepository = vueloRepository;
        this.aerolineaRepository = aerolineaRepository;
        this.rutaRepository = rutaRepository;
        this.vueloMapper = vueloMapper;
    }

    @Transactional
    public VueloDto crearVuelo(CreateVueloDto createDto) {
        vueloRepository.findByAerolineaIdAndNumeroVuelo(createDto.aerolineaId(), createDto.numeroVuelo()).ifPresent(v -> {
            throw new DuplicateResourceException("El número de vuelo '" + createDto.numeroVuelo() + "' ya existe para esta aerolínea.");
        });

        Aerolinea aerolinea = aerolineaRepository.findById(createDto.aerolineaId())
                .orElseThrow(() -> new ResourceNotFoundException("Aerolinea", "id", createDto.aerolineaId()));

        Ruta ruta = rutaRepository.findById(createDto.rutaId())
                .orElseThrow(() -> new ResourceNotFoundException("Ruta", "id", createDto.rutaId()));

        Vuelo nuevoVuelo = new Vuelo();
        nuevoVuelo.setNumeroVuelo(createDto.numeroVuelo());
        nuevoVuelo.setAerolinea(aerolinea);
        nuevoVuelo.setRuta(ruta);
        nuevoVuelo.setDuracionMin(createDto.duracionMin());

        return vueloMapper.toDto(vueloRepository.save(nuevoVuelo));
    }

    @Transactional(readOnly = true)
    public List<VueloDto> obtenerTodos() {
        return vueloRepository.findAll().stream().map(vueloMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VueloDto obtenerPorId(Long id) {
        return vueloRepository.findById(id).map(vueloMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vuelo", "id", id));
    }

    @Transactional
    public void eliminarVuelo(Long id) {
        if (!vueloRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vuelo", "id", id);
        }
        vueloRepository.deleteById(id);
    }
}