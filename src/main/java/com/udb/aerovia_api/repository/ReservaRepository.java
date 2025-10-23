package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Reserva;
import com.udb.aerovia_api.domain.enums.EstadoReserva;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ReservaRepository extends JpaRepository<Reserva, Long>, JpaSpecificationExecutor<Reserva> {
    Optional<Reserva> findByCodigoReserva(String codigoReserva); // Para buscar por código
    List<Reserva> findByUsuarioId(Long usuarioId); // Para buscar por usuario
    Long countByEstado(EstadoReserva estado); // Añadir este método
}
