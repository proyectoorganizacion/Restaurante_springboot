package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoriaRequest {
    @NotBlank
    private String nombre;
    private String descripcion;
}
