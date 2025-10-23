package com.udb.aerovia_api.domain;

import com.udb.aerovia_api.domain.enums.EstadoReclamo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reclamos")
public class Reclamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reclamo")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reserva_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reclamos_reserva"))
    private Reserva reserva;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pasajero_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reclamos_pasajero"))
    private Pasajero pasajero;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoReclamo estado;
}