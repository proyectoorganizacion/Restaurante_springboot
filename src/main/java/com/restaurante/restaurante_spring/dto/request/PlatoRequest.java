package com.restaurante.restaurante_spring.dto.request;

import com.restaurante.restaurante_spring.entity.CategoriaPlatoEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlatoRequest {

    @NotBlank(message = "El nombre del plato es obligatorio.")
    private String nombre;

    @NotNull(message = "El precio es obligatorio.")
    @Positive(message = "El precio debe ser un número entero positivo mayor a 0.")
    private Integer precio;

    @NotBlank(message = "La descripción es obligatoria.")
    private String descripcion;

    @NotBlank(message = "La URL de la imagen es obligatoria.")
    private String urlImagen;

    @NotNull(message = "La categoría es obligatoria.")
    private CategoriaPlatoEntity categoria;

    @NotNull(message = "Todo plato debe estar asociado a un restaurante.")
    private Long idRestaurante;
}
