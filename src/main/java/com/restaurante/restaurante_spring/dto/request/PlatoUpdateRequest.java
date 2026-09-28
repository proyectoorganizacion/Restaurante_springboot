package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoUpdateRequest {

    // Opcional: si viene, debe ser positivo (misma regla que en creación).
    @Positive(message = "El precio debe ser un número entero positivo mayor a 0.")
    private Integer precio;

    // Opcional
    private String descripcion;
}
