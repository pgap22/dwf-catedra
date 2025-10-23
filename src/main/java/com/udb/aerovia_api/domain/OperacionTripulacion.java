package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operacion_tripulacion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"operacion_id", "tripulante_id"}, name = "uq_trip_por_operacion")
})
public class OperacionTripulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion_trip")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "operacion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_optrip_operacion"))
    private OperacionVuelo operacionVuelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tripulante_id", nullable = false, foreignKey = @ForeignKey(name = "fk_optrip_trip"))
    private Tripulante tripulante;

    @Column(name = "rol_en_vuelo", nullable = false, length = 50)
    private String rolEnVuelo; // Ej: PILOTO, TCP
}