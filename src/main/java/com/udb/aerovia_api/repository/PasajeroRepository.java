package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
    List<Pasajero> findByUsuarioId(Long usuarioId);
}
