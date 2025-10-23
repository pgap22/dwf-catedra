package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aviones", uniqueConstraints = {
    @UniqueConstraint(columnNames = "matricula")
})
public class Avion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_avion")
    private Long id;

    @Column(nullable = false, length = 20)
    private String matricula;

    @Column(nullable = false, length = 80)
    private String modelo;

    @Column(name = "capacidad_total", nullable = false)
    private Integer capacidadTotal;
}