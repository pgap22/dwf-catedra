package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.AsientoAvion;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AsientoAvionRepository extends JpaRepository<AsientoAvion, Long> {
    Optional<AsientoAvion> findByAvionIdAndCodigoAsiento(Long avionId, String codigoAsiento);
    List<AsientoAvion> findByAvionId(Long avionId); // Para listar asientos de un avión
}