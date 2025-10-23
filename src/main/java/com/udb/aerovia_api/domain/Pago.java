package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reserva_id", nullable = false, foreignKey = @ForeignKey(name = "fk_pagos_reserva"))
    private Reserva reserva;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDateTime fechaPago;

    @Column(name = "metodo_pago", nullable = false, length = 40)
    private String metodoPago;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;
}