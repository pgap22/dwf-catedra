package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "origen_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rutas_origen"))
    private Aeropuerto origen;

    @ManyToOne(optional = false)
    @JoinColumn(name = "destino_id", nullable = false, foreignKey = @ForeignKey(name = "fk_rutas_destino"))
    private Aeropuerto destino;

    @Column(name = "distancia_km", nullable = false)
    private Integer distanciaKm;
}