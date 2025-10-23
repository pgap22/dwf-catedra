package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Tripulante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripulanteRepository extends JpaRepository<Tripulante, Long> {
}