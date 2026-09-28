package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PlatoRequest {
    @NotBlank
    private String nombre;
    @NotBlank
    private Double precio;
    private String descripcion;
    private String urlImagen;
    private Boolean estado;
    @NotBlank
    private Integer categoria;
}
