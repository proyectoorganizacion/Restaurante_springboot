package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoRequest {

    @NotBlank
    private String nombre;

    @NotNull
    private Double precio;

    private String descripcion;

    private String urlImagen;

    private Boolean estado;

    @NotNull
    private Integer categoria;

    @NotNull
    private Integer idRestaurante;
}