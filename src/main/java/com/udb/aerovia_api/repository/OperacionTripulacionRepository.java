package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.OperacionTripulacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperacionTripulacionRepository extends JpaRepository<OperacionTripulacion, Long> {

    List<OperacionTripulacion> findByOperacionVueloId(Long operacionId);

    boolean existsByOperacionVueloIdAndTripulanteId(Long operacionId, Long tripulanteId);
}
