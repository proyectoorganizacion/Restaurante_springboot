package com.restaurante.restaurante_spring.dto.response;

import com.restaurante.restaurante_spring.entity.CategoriaPlatoEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlatoResponse {
    private Long id;
    private String nombre;
    private Integer precio;
    private String descripcion;
    private String urlImagen;
    private CategoriaPlatoEntity categoria;
    private Long idRestaurante;
    private Boolean activo;
}
