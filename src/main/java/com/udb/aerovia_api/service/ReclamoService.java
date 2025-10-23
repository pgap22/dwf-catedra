package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Pasajero;
import com.udb.aerovia_api.domain.Reclamo;
import com.udb.aerovia_api.domain.Reserva;
import com.udb.aerovia_api.domain.enums.EstadoReclamo;
import com.udb.aerovia_api.dto.CreateReclamoDto;
import com.udb.aerovia_api.dto.ReclamoDto;
import com.udb.aerovia_api.dto.UpdateReclamoEstadoDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.ReclamoMapper;
import com.udb.aerovia_api.repository.PasajeroRepository;
import com.udb.aerovia_api.repository.ReclamoRepository;
import com.udb.aerovia_api.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReclamoService {

    private final ReclamoRepository reclamoRepository;
    private final ReservaRepository reservaRepository;
    private final PasajeroRepository pasajeroRepository;
    private final ReclamoMapper reclamoMapper;

    public ReclamoService(ReclamoRepository reclamoRepository, ReservaRepository reservaRepository, PasajeroRepository pasajeroRepository, ReclamoMapper reclamoMapper) {
        this.reclamoRepository = reclamoRepository;
        this.reservaRepository = reservaRepository;
        this.pasajeroRepository = pasajeroRepository;
        this.reclamoMapper = reclamoMapper;
    }

    @Transactional
    public ReclamoDto crearReclamo(CreateReclamoDto dto) {
        Reserva reserva = reservaRepository.findById(dto.reservaId())
                .orElseThrow(() -> new ResourceNotFoundException("Reserva", "id", dto.reservaId()));
        Pasajero pasajero = pasajeroRepository.findById(dto.pasajeroId())
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", dto.pasajeroId()));

        // Aquí se podría validar que el pasajero pertenezca a la reserva

        Reclamo nuevoReclamo = new Reclamo();
        nuevoReclamo.setReserva(reserva);
        nuevoReclamo.setPasajero(pasajero);
        nuevoReclamo.setDescripcion(dto.descripcion());
        nuevoReclamo.setEstado(EstadoReclamo.ABIERTO); // Estado inicial por defecto

        return reclamoMapper.toDto(reclamoRepository.save(nuevoReclamo));
    }

    @Transactional(readOnly = true)
    public List<ReclamoDto> obtenerReclamosPorReserva(Long reservaId) {
        if (!reservaRepository.existsById(reservaId)) {
            throw new ResourceNotFoundException("Reserva", "id", reservaId);
        }
        return reclamoRepository.findByReservaId(reservaId).stream()
                .map(reclamoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ReclamoDto> obtenerReclamosPorPasajero(Long pasajeroId) {
        if (!pasajeroRepository.existsById(pasajeroId)) {
            throw new ResourceNotFoundException("Pasajero", "id", pasajeroId);
        }
         return reclamoRepository.findByPasajeroId(pasajeroId).stream()
                .map(reclamoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ReclamoDto obtenerReclamoPorId(Long id) {
         return reclamoRepository.findById(id)
                .map(reclamoMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Reclamo", "id", id));
    }

    @Transactional
    public ReclamoDto actualizarEstadoReclamo(Long id, UpdateReclamoEstadoDto dto) {
        Reclamo reclamo = reclamoRepository.findById(id)
                 .orElseThrow(() -> new ResourceNotFoundException("Reclamo", "id", id));

        // Lógica de negocio: Validar transiciones de estado si es necesario
        // (ej. no se puede pasar de CERRADO a ABIERTO)

        reclamo.setEstado(dto.estado());
        return reclamoMapper.toDto(reclamoRepository.save(reclamo));
    }
}