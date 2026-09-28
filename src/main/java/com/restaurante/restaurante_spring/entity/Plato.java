package com.restaurante.restaurante_spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "plato")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer precio;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(name = "url_imagen", nullable = false)
    private String urlImagen;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaPlatoEntity categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_restaurante", nullable = false)
    private Restaurante restaurante;

    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;
}

