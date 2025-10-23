package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Aeropuerto;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AeropuertoRepository extends JpaRepository<Aeropuerto, Long> {
    Optional<Aeropuerto> findByCodigoIata(String codigoIata); // AÑADIR ESTA LÍNEA
}