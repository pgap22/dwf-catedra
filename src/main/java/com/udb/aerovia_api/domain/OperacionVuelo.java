package com.udb.aerovia_api.domain;

import com.udb.aerovia_api.domain.enums.EstadoOperacionVuelo;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operacion_vuelo")
public class OperacionVuelo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_operacion")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vuelo_id", nullable = false, foreignKey = @ForeignKey(name = "fk_operacion_vuelo_vuelo"))
    private Vuelo vuelo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "avion_id", nullable = false, foreignKey = @ForeignKey(name = "fk_operacion_vuelo_avion"))
    private Avion avion;

    @Column(name = "fecha_salida", nullable = false)
    private LocalDateTime fechaSalida;

    @Column(name = "fecha_llegada", nullable = false)
    private LocalDateTime fechaLlegada;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoOperacionVuelo estado;
}