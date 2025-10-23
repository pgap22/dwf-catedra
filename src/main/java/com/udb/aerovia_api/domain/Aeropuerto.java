package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aeropuertos", uniqueConstraints = {
    @UniqueConstraint(columnNames = "codigo_iata")
})
public class Aeropuerto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aeropuerto")
    private Long id;

    @Column(name = "codigo_iata", nullable = false, length = 3)
    private String codigoIata;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String ciudad;

    @Column(nullable = false, length = 120)
    private String pais;
}