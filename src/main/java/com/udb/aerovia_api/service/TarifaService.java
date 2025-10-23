package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Clase;
import com.udb.aerovia_api.domain.Tarifa;
import com.udb.aerovia_api.dto.CreateTarifaDto;
import com.udb.aerovia_api.dto.TarifaDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.TarifaMapper;
import com.udb.aerovia_api.repository.ClaseRepository;
import com.udb.aerovia_api.repository.TarifaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TarifaService {

    private final TarifaRepository tarifaRepository;
    private final ClaseRepository claseRepository;
    private final TarifaMapper tarifaMapper;

    public TarifaService(TarifaRepository tarifaRepository, ClaseRepository claseRepository, TarifaMapper tarifaMapper) {
        this.tarifaRepository = tarifaRepository;
        this.claseRepository = claseRepository;
        this.tarifaMapper = tarifaMapper;
    }

    @Transactional
    public TarifaDto crearTarifa(CreateTarifaDto dto) {
        tarifaRepository.findByCodigo(dto.codigo()).ifPresent(t -> {
            throw new DuplicateResourceException("El código de tarifa '" + dto.codigo() + "' ya existe.");
        });

        Clase clase = claseRepository.findById(dto.claseId())
            .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", dto.claseId()));

        Tarifa nuevaTarifa = new Tarifa();
        nuevaTarifa.setCodigo(dto.codigo());
        nuevaTarifa.setClase(clase);
        nuevaTarifa.setReembolsable(dto.reembolsable());

        return tarifaMapper.toDto(tarifaRepository.save(nuevaTarifa));
    }

    @Transactional(readOnly = true)
    public List<TarifaDto> obtenerTodas() {
        return tarifaRepository.findAll().stream()
                .map(tarifaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TarifaDto obtenerPorId(Long id) {
        return tarifaRepository.findById(id)
                .map(tarifaMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa", "id", id));
    }

    @Transactional
    public TarifaDto actualizarTarifa(Long id, CreateTarifaDto dto) {
        Tarifa tarifaExistente = tarifaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarifa", "id", id));

        tarifaRepository.findByCodigo(dto.codigo()).ifPresent(t -> {
             if (!t.getId().equals(id)) {
                 throw new DuplicateResourceException("El código de tarifa '" + dto.codigo() + "' ya está en uso.");
             }
        });

        Clase clase = claseRepository.findById(dto.claseId())
            .orElseThrow(() -> new ResourceNotFoundException("Clase", "id", dto.claseId()));

        tarifaExistente.setCodigo(dto.codigo());
        tarifaExistente.setClase(clase);
        tarifaExistente.setReembolsable(dto.reembolsable());

        return tarifaMapper.toDto(tarifaRepository.save(tarifaExistente));
    }

    @Transactional
    public void eliminarTarifa(Long id) {
         if (!tarifaRepository.existsById(id)) {
             throw new ResourceNotFoundException("Tarifa", "id", id);
         }
         // Cuidado: Podríamos necesitar verificar si esta tarifa está en uso
         // en alguna TarifaOperacion antes de borrar.
         tarifaRepository.deleteById(id);
    }
}