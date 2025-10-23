package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Vuelo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VueloRepository extends JpaRepository<Vuelo, Long> {
    Optional<Vuelo> findByAerolineaIdAndNumeroVuelo(Long aerolineaId, String numeroVuelo);
}