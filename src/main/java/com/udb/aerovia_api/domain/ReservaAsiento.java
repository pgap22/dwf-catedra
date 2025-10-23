package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reserva_asiento", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"operacion_id", "asiento_avion_id"}, name = "uq_asiento_por_operacion")
})
public class ReservaAsiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva_asiento")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reserva_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ras_reserva"))
    private Reserva reserva;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pasajero_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ras_pasajero"))
    private Pasajero pasajero;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ras_operacion"))
    private OperacionVuelo operacionVuelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "asiento_avion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ras_asiento"))
    private AsientoAvion asientoAvion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tarifa_oper_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ras_tar_oper"))
    private TarifaOperacion tarifaOperacion;

    @Column(name = "precio_pagado", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioPagado;
}