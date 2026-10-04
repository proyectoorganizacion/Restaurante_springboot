package com.restaurante.restaurante_spring.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class PedidoResponse {
    private Integer idPedido;
    private LocalDateTime fechaCreacion;
    private String estado;
    private String mensaje;
}