package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.TarifaOperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TarifaOperacionRepository extends JpaRepository<TarifaOperacion, Long> {
    Optional<TarifaOperacion> findByOperacionVueloIdAndTarifaId(Long operacionId, Long tarifaId);
    List<TarifaOperacion> findByOperacionVueloId(Long operacionId);
}