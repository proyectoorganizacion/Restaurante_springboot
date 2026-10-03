package com.restaurante.restaurante_spring.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="plato")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable=false)
    private String nombre;
    @Column(nullable=false)
    private Double precio;
    private String descripcion;
    private String urlImagen;
    @Column(name = "activo", nullable = false)
    private Boolean estado;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante", nullable = false)
    private Restaurante restaurante;
}
