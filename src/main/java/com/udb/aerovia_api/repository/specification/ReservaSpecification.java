package com.udb.aerovia_api.repository.specification;

import com.udb.aerovia_api.domain.Reserva;
import com.udb.aerovia_api.domain.ReservaAsiento;
import com.udb.aerovia_api.domain.enums.EstadoReserva;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public final class ReservaSpecification {

    private ReservaSpecification() {
    }

    public static Specification<Reserva> hasEstado(EstadoReserva estado) {
        return (root, query, builder) -> estado == null ? null : builder.equal(root.get("estado"), estado);
    }

    public static Specification<Reserva> fechaDesde(LocalDateTime desde) {
        return (root, query, builder) -> desde == null ? null : builder.greaterThanOrEqualTo(root.get("fechaReserva"), desde);
    }

    public static Specification<Reserva> fechaHasta(LocalDateTime hasta) {
        return (root, query, builder) -> hasta == null ? null : builder.lessThanOrEqualTo(root.get("fechaReserva"), hasta);
    }

    public static Specification<Reserva> hasAerolinea(Long aerolineaId) {
        return (root, query, builder) -> {
            if (aerolineaId == null) {
                return null;
            }
            query.distinct(true);
            Join<Reserva, ReservaAsiento> asientos = root.join("asientos", JoinType.LEFT);
            Join<?, ?> operacion = asientos.join("operacionVuelo", JoinType.LEFT);
            Join<?, ?> vuelo = operacion.join("vuelo", JoinType.LEFT);
            Join<?, ?> aerolinea = vuelo.join("aerolinea", JoinType.LEFT);
            return builder.equal(aerolinea.get("id"), aerolineaId);
        };
    }

    public static Specification<Reserva> hasOrigen(Long origenId) {
        return (root, query, builder) -> {
            if (origenId == null) {
                return null;
            }
            query.distinct(true);
            Join<Reserva, ReservaAsiento> asientos = root.join("asientos", JoinType.LEFT);
            Join<?, ?> operacion = asientos.join("operacionVuelo", JoinType.LEFT);
            Join<?, ?> vuelo = operacion.join("vuelo", JoinType.LEFT);
            Join<?, ?> ruta = vuelo.join("ruta", JoinType.LEFT);
            Join<?, ?> origen = ruta.join("origen", JoinType.LEFT);
            return builder.equal(origen.get("id"), origenId);
        };
    }

    public static Specification<Reserva> hasDestino(Long destinoId) {
        return (root, query, builder) -> {
            if (destinoId == null) {
                return null;
            }
            query.distinct(true);
            Join<Reserva, ReservaAsiento> asientos = root.join("asientos", JoinType.LEFT);
            Join<?, ?> operacion = asientos.join("operacionVuelo", JoinType.LEFT);
            Join<?, ?> vuelo = operacion.join("vuelo", JoinType.LEFT);
            Join<?, ?> ruta = vuelo.join("ruta", JoinType.LEFT);
            Join<?, ?> destino = ruta.join("destino", JoinType.LEFT);
            return builder.equal(destino.get("id"), destinoId);
        };
    }
}
