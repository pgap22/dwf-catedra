package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarifa", uniqueConstraints = {
    @UniqueConstraint(columnNames = "codigo", name = "uq_tarifa_codigo")
})
public class Tarifa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Long id;

    @Column(nullable = false, length = 20)
    private String codigo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clase_id", nullable = false, foreignKey = @ForeignKey(name = "fk_tarifa_clase"))
    private Clase clase;

    @Column(nullable = false)
    private boolean reembolsable;
}