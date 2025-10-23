package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Tarifa;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findByCodigo(String codigo);
}