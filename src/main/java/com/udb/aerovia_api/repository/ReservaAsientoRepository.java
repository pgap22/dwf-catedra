package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.ReservaAsiento;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaAsientoRepository extends JpaRepository<ReservaAsiento, Long> {
    List<ReservaAsiento> findByReservaId(Long reservaId); // Para cargar los asientos de una reserva

    boolean existsByOperacionVueloIdAndAsientoAvionIdAndReservaIdNot(Long operacionId, Long asientoAvionId, Long reservaId);
}
