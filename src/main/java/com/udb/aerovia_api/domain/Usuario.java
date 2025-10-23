package com.udb.aerovia_api.domain;

import com.udb.aerovia_api.domain.enums.Rol;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {
    @UniqueConstraint(columnNames = "correo")
})
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 180)
    private String correo;

    @Column(name = "clave_hash", nullable = false, length = 255)
    private String claveHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Rol rol;

    @Column(nullable = false)
    private boolean activo = true;
}
