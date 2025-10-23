package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.AsientoAvion;
import com.udb.aerovia_api.domain.Avion;
import com.udb.aerovia_api.domain.Clase;
import com.udb.aerovia_api.dto.AsientoAvionDto;
import com.udb.aerovia_api.dto.CreateAsientoAvionDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.AsientoAvionMapper;
import com.udb.aerovia_api.repository.AsientoAvionRepository;
import com.udb.aerovia_api.repository.AvionRepository;
import com.udb.aerovia_api.repository.ClaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AsientoAvionService {

    private final AsientoAvionRepository asientoAvionRepository;
    private final AvionRepository avionRepository;
    private final ClaseRepository claseRepository;
    private final AsientoAvionMapper mapper;

    public AsientoAvionService(AsientoAvionRepository asientoAvionRepository, AvionRepository avionRepository, ClaseRepository claseRepository, AsientoAvionMapper mapper) {
        this.asientoAvionRepository = asientoAvionRepository;
        this.avionRepository = avionRepository;
        this.claseRepository = claseRepository;
        this.mapper = mapper;
    }

    @Transactional
    public AsientoAvionDto crearAsiento(CreateAsientoAvionDto dto) {
        asientoAvionRepository.findByAvionIdAndCodigoAsiento(dto.avionId(), dto.codigoAsiento()).ifPresent(a -> {
            throw new DuplicateResourceException("El código de asiento '" + dto.codigoAsiento() + "' ya existe para este avión.");
        });

        Avion avion = avionRepository.findById(dto.avionId())
            .orElseThrow(() -> new ResourceNotFoundException("Avion", "id", dto.avionId()));
        Clase clase = claseRepository.findById(dto.claseId())
            .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", dto.claseId()));

        AsientoAvion nuevoAsiento = new AsientoAvion();
        nuevoAsiento.setAvion(avion);
        nuevoAsiento.setCodigoAsiento(dto.codigoAsiento());
        nuevoAsiento.setClase(clase);

        return mapper.toDto(asientoAvionRepository.save(nuevoAsiento));
    }

    @Transactional(readOnly = true)
    public List<AsientoAvionDto> obtenerAsientosPorAvion(Long avionId) {
        if (!avionRepository.existsById(avionId)) {
            throw new ResourceNotFoundException("Avion", "id", avionId);
        }
        return asientoAvionRepository.findByAvionId(avionId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AsientoAvionDto obtenerAsientoPorId(Long id) {
         return asientoAvionRepository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("AsientoAvion", "id", id));
    }

    @Transactional
    public AsientoAvionDto actualizarAsiento(Long id, CreateAsientoAvionDto dto) {
        AsientoAvion existente = asientoAvionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("AsientoAvion", "id", id));

        // Validar que no se intente cambiar a un asiento que ya existe en el mismo avión
        asientoAvionRepository.findByAvionIdAndCodigoAsiento(dto.avionId(), dto.codigoAsiento()).ifPresent(a -> {
            if (!a.getId().equals(id)) {
                 throw new DuplicateResourceException("El código de asiento '" + dto.codigoAsiento() + "' ya está en uso en este avión.");
            }
        });

        // Generalmente no se cambia el avión de un asiento, pero sí la clase o código
         if (!existente.getAvion().getId().equals(dto.avionId())) {
             throw new IllegalArgumentException("No se puede cambiar el avión de un asiento existente.");
         }

         Clase clase = claseRepository.findById(dto.claseId())
            .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", dto.claseId()));

        existente.setCodigoAsiento(dto.codigoAsiento());
        existente.setClase(clase);

        return mapper.toDto(asientoAvionRepository.save(existente));
    }


    @Transactional
    public void eliminarAsiento(Long id) {
         if (!asientoAvionRepository.existsById(id)) {
             throw new ResourceNotFoundException("AsientoAvion", "id", id);
         }
         // Cuidado: Validar si el asiento está reservado antes de borrar
         asientoAvionRepository.deleteById(id);
    }
}