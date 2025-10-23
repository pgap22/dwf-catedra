package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Reclamo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReclamoRepository extends JpaRepository<Reclamo, Long> {
    List<Reclamo> findByReservaId(Long reservaId);
    List<Reclamo> findByPasajeroId(Long pasajeroId);
}