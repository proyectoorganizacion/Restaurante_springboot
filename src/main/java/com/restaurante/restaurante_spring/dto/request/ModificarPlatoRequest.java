package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModificarPlatoRequest {

    @NotNull
    private Double precio;

    private String descripcion;
}