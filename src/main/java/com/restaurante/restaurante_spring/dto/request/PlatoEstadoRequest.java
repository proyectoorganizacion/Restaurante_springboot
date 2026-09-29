package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoEstadoRequest {
    @NotNull(message = "El campo activo es obligatorio.")
    private Boolean activo;
}