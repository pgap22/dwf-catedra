package com.udb.aerovia_api.repository.specification;

import com.udb.aerovia_api.domain.*;
import com.udb.aerovia_api.domain.enums.EstadoOperacionVuelo;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OperacionVueloSpecification {

    public static Specification<OperacionVuelo> buscarPorCriterios(
            Long origenId, Long destinoId, LocalDate fechaSalida) {

        return (Root<OperacionVuelo> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            // JOINs necesarios para filtrar
            Join<OperacionVuelo, Vuelo> vueloJoin = root.join("vuelo", JoinType.INNER);
            Join<Vuelo, Ruta> rutaJoin = vueloJoin.join("ruta", JoinType.INNER);
            Join<Ruta, Aeropuerto> origenJoin = rutaJoin.join("origen", JoinType.INNER);
            Join<Ruta, Aeropuerto> destinoJoin = rutaJoin.join("destino", JoinType.INNER);

            List<Predicate> predicates = new ArrayList<>();

            predicates.add(cb.equal(origenJoin.get("id"), origenId));
            predicates.add(cb.equal(destinoJoin.get("id"), destinoId));

            // Filtrar por el día completo
            LocalDateTime inicioDia = fechaSalida.atStartOfDay();
            LocalDateTime finDia = fechaSalida.atTime(LocalTime.MAX);
            predicates.add(cb.between(root.get("fechaSalida"), inicioDia, finDia));

            // Asegurarse de que el estado no sea CANCELADO
            predicates.add(cb.notEqual(root.get("estado"), EstadoOperacionVuelo.CANCELADO));

             query.distinct(true); // Evita duplicados si hay múltiples tarifas
             root.fetch("vuelo", JoinType.INNER).fetch("aerolinea", JoinType.INNER);
             root.fetch("vuelo", JoinType.INNER).fetch("ruta", JoinType.INNER).fetch("origen", JoinType.INNER);
             root.fetch("vuelo", JoinType.INNER).fetch("ruta", JoinType.INNER).fetch("destino", JoinType.INNER);
             root.fetch("avion", JoinType.INNER);


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}