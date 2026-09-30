package com.restaurante.restaurante_spring.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder

public class CategoriaResponse {
    private String nombre;
    private String descripcion;
}
