package com.restaurante.restaurante_spring.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRegisterResponse {

    private Integer id;

    private String nombre;

    private String apellido;

    private String docIdentidad;

    private String celular;

    private String correo;

    private String rol;

    private String mensaje;
}