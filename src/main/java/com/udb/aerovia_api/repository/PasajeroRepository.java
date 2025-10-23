package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Pasajero;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {
}