package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Cancelacion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CancelacionRepository extends JpaRepository<Cancelacion, Long> {
    // Podríamos añadir métodos para buscar por reserva si fuera necesario
    Optional<Cancelacion> findByReservaId(Long reservaId);
}