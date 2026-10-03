package com.restaurante.restaurante_spring.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurante")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "nit", nullable = false, unique = true, length = 20)
    private String nit;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    @Column(name = "telefono", length = 13)
    private String telefono;

    @Column(name = "url_logo", length = 255)
    private String url_logo;

    @Column(name = "id_propietario", nullable = false)
    private Integer id_propietario;
}