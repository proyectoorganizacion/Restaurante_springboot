package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.PedidoRequest;
import com.restaurante.restaurante_spring.dto.response.PedidoResponse;
import org.springframework.data.domain.Page;

public interface PedidoService {
    PedidoResponse crearPedido(PedidoRequest request);
    // HU-12: Obtener lista de pedidos filtrando por estado con paginación
    Page<PedidoResponse> listarPedidosPorEstado(String estado, int page, int size);
}