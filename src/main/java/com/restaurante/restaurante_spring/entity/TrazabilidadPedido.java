
package com.restaurante.restaurante_spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trazabilidad_pedido")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrazabilidadPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Column(name = "estado_anterior", length = 50)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", nullable = false, length = 50)
    private String estadoNuevo;

    @Column(name = "fecha_cambio", nullable = false)
    private LocalDateTime fechaCambio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario_cambio", nullable = false)
    private Usuario usuarioCambio;
}
