package com.restaurante.restaurante_spring.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class PedidoRequest {

    @NotNull(message = "El id del restaurante es obligatorio")
    private Integer idRestaurante;

    @NotEmpty(message = "El pedido debe tener al menos un plato")
    @Valid
    private List<PedidoPlatoRequest> platos;
}