package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.PedidoRequest;
import com.restaurante.restaurante_spring.dto.response.PedidoResponse;

public interface PedidoService {
    PedidoResponse crearPedido(PedidoRequest request);
}