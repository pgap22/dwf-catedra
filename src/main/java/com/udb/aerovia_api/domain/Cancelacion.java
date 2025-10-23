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
@Table(name = "cancelaciones")
public class Cancelacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cancelacion")
    private Long id;

    @OneToOne(optional = false) // Una reserva solo puede tener una cancelación
    @JoinColumn(name = "reserva_id", nullable = false, foreignKey = @ForeignKey(name = "fk_cancel_reserva"))
    private Reserva reserva;

    @Column(name = "fecha_cancelacion", nullable = false)
    private LocalDateTime fechaCancelacion;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal cargo = BigDecimal.ZERO; // Valor por defecto

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal reembolso = BigDecimal.ZERO; // Valor por defecto
}