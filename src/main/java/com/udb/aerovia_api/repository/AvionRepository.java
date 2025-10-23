package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Avion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AvionRepository extends JpaRepository<Avion, Long> {
    Optional<Avion> findByMatricula(String matricula); // AÑADIR ESTA LÍNEA
}