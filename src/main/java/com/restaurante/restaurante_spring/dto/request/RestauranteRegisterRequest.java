package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RestauranteRegisterRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(
            regexp = "^(?=.*[a-zA-ZáéíóúÁÉÍÓÚñÑ]).+$",
            message = "El nombre puede contener números, pero no puede ser únicamente numérico"
    )
    private String nombre;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El NIT debe ser únicamente numérico")
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 13, message = "El teléfono debe contener máximo 13 caracteres")
    @Pattern(
            regexp = "^\\+?[0-9]+$",
            message = "El teléfono debe ser numérico y solo puede contener '+' al inicio"
    )
    private String telefono;

    @NotBlank(message = "La URL del logo es obligatoria")
    private String urlLogo;

    @NotNull(message = "El ID del propietario es obligatorio")
    private Integer idPropietario;
}