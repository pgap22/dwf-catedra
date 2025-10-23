package com.udb.aerovia_api.repository;

import com.udb.aerovia_api.domain.Pago;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PagoRepository extends JpaRepository<Pago, Long>, JpaSpecificationExecutor<Pago> {
    List<Pago> findByReservaId(Long reservaId); 
}
