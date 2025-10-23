package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.enums.EstadoReserva;
import com.udb.aerovia_api.dto.EstadisticasDto;
import com.udb.aerovia_api.repository.CancelacionRepository;
import com.udb.aerovia_api.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstadisticasService {

    private final ReservaRepository reservaRepository;
    private final CancelacionRepository cancelacionRepository;

    public EstadisticasService(ReservaRepository reservaRepository, CancelacionRepository cancelacionRepository) {
        this.reservaRepository = reservaRepository;
        this.cancelacionRepository = cancelacionRepository;
    }

    @Transactional(readOnly = true)
    public EstadisticasDto obtenerEstadisticasBasicas() {
        // Usamos métodos count() de los repositorios o filtramos en memoria (count es más eficiente)
        // Necesitamos añadir métodos countByEstado al ReservaRepository

        long activas = reservaRepository.countByEstado(EstadoReserva.ACTIVA);
        long canceladas = reservaRepository.countByEstado(EstadoReserva.CANCELADA);
        long completadas = reservaRepository.countByEstado(EstadoReserva.COMPLETADA);
        long totalCancelaciones = cancelacionRepository.count();

        return new EstadisticasDto(activas, canceladas, completadas, totalCancelaciones);
    }
}