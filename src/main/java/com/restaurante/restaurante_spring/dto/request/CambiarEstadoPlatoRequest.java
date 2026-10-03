package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambiarEstadoPlatoRequest {

    @NotNull(message = "El estado es obligatorio")
    private Boolean estado;
}