package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asientos_avion", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"avion_id", "codigo_asiento"}, name = "uq_asientos_por_avion")
})
public class AsientoAvion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asiento")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "avion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_asientos_avion_avion"))
    private Avion avion;

    @Column(name = "codigo_asiento", nullable = false, length = 10)
    private String codigoAsiento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clase_id", nullable = false, foreignKey = @ForeignKey(name = "fk_asientos_avion_clase"))
    private Clase clase;
}