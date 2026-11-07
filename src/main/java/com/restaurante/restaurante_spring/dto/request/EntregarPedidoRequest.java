package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

// HU-15: Datos que envía el empleado para marcar un pedido como ENTREGADO
@Getter
@Setter
public class EntregarPedidoRequest {

    @NotBlank(message = "El PIN de entrega es obligatorio")
    @Pattern(regexp = "\\d{4}", message = "El PIN debe tener exactamente 4 dígitos")
    private String pin;
}
