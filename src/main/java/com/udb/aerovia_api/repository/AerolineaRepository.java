package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Aerolinea;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AerolineaRepository extends JpaRepository<Aerolinea, Long> {
    Optional<Aerolinea> findByCodigoIata(String codigoIata); 
}