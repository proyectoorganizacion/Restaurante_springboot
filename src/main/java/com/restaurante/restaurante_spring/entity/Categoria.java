package com.restaurante.restaurante_spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {
    @Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;
    @Column(nullable = false)
    private String nombre;
    private String descripcion;
}
