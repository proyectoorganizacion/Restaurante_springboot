package com.restaurante.restaurante_spring.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String name;

    @Column(name = "apellido", nullable = false, length = 100)
    private String lastname;

    @Column(name = "doc_identidad", nullable = false, unique = true, length = 20)
    private String identification;

    @Column(name = "celular", nullable = false, length = 13)
    private String phone;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate birthdate;

    @Column(name = "correo", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;
}