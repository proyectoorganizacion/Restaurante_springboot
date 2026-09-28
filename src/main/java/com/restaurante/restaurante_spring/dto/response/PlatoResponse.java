package com.restaurante.restaurante_spring.dto.response;


import com.restaurante.restaurante_spring.entity.Categoria;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PlatoResponse {
    private String nombre;
    private String descripcion;
    private Double precio;
    private String urlImagen;
    private Boolean estado;
    private Categoria categoria;
}
