package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vuelo", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aerolinea_id", "numero_vuelo"}, name = "uq_vuelo_num_por_aerolinea")
})
public class Vuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vuelo")
    private Long id;

    @Column(name = "numero_vuelo", nullable = false, length = 10)
    private String numeroVuelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aerolinea_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vuelo_aerolinea"))
    private Aerolinea aerolinea;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ruta_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vuelo_ruta"))
    private Ruta ruta;

    @Column(name = "duracion_min", nullable = false)
    private Integer duracionMin;
}