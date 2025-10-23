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
@Table(name = "tarifa_operacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"operacion_id", "tarifa_id"}, name = "uq_tarifa_por_operacion")
})
public class TarifaOperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa_oper")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tarifa_oper_operacion"))
    private OperacionVuelo operacionVuelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tarifa_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tarifa_oper_tarifa"))
    private Tarifa tarifa;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precio;

    @Column(name = "asientos_disponibles", nullable = false)
    private Integer asientosDisponibles;

    @Version
    private Integer version;
}