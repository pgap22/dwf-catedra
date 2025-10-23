package com.udb.aerovia_api.domain;

import com.udb.aerovia_api.domain.enums.EstadoReserva;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "asientos")
@ToString(exclude = "asientos")
@Entity
@Table(name = "reservas", uniqueConstraints = {
    @UniqueConstraint(columnNames = "codigo_reserva", name = "uq_reservas_codigo")
})
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long id;

    @Column(name = "codigo_reserva", nullable = false, length = 12)
    private String codigoReserva;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservas_usuario"))
    private Usuario usuario;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDateTime fechaReserva;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoReserva estado;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "reserva", fetch = FetchType.LAZY)
    private List<ReservaAsiento> asientos = new ArrayList<>();
}
