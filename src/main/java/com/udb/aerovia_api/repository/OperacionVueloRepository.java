package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.OperacionVuelo;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperacionVueloRepository extends JpaRepository<OperacionVuelo, Long>, JpaSpecificationExecutor<OperacionVuelo> {

    @Override
    @EntityGraph(attributePaths = {
            "vuelo",
            "vuelo.ruta",
            "vuelo.ruta.origen",
            "vuelo.ruta.destino",
            "vuelo.aerolinea",
            "avion"
    })
    List<OperacionVuelo> findAll();
}
