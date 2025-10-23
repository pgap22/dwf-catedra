package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.OperacionVuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Importar

// Añadir JpaSpecificationExecutor<OperacionVuelo>
public interface OperacionVueloRepository extends JpaRepository<OperacionVuelo, Long>, JpaSpecificationExecutor<OperacionVuelo> {
}