package com.udb.aerovia_api.repository.specification;

import com.udb.aerovia_api.domain.Pago;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

public final class PagoSpecification {

    private PagoSpecification() {
    }

    public static Specification<Pago> byReservaId(Long reservaId) {
        return (root, query, builder) -> reservaId == null ? null : builder.equal(root.get("reserva").get("id"), reservaId);
    }

    public static Specification<Pago> byCodigoReserva(String codigoReserva) {
        return (root, query, builder) -> codigoReserva == null || codigoReserva.isBlank()
                ? null
                : builder.equal(root.get("reserva").get("codigoReserva"), codigoReserva);
    }

    public static Specification<Pago> fechaDesde(LocalDateTime desde) {
        return (root, query, builder) -> desde == null ? null : builder.greaterThanOrEqualTo(root.get("fechaPago"), desde);
    }

    public static Specification<Pago> fechaHasta(LocalDateTime hasta) {
        return (root, query, builder) -> hasta == null ? null : builder.lessThanOrEqualTo(root.get("fechaPago"), hasta);
    }
}
