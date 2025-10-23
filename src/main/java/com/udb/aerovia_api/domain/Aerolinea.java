package com.udb.aerovia_api.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "aerolineas", uniqueConstraints = {
    @UniqueConstraint(columnNames = "codigo_iata")
})
public class Aerolinea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aerolinea")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "codigo_iata", nullable = false, length = 3)
    private String codigoIata;
}