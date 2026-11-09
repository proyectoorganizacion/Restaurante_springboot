package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.EntregarPedidoRequest;
import com.restaurante.restaurante_spring.dto.request.PedidoRequest;
import com.restaurante.restaurante_spring.dto.response.PedidoResponse;
import org.springframework.data.domain.Page;

import com.restaurante.restaurante_spring.dto.response.TrazabilidadPedidoResponse;
import java.util.List;

public interface PedidoService {
    PedidoResponse crearPedido(PedidoRequest request);
    // HU-12: Obtener lista de pedidos filtrando por estado con paginación
    Page<PedidoResponse> listarPedidosPorEstado(String estado, int page, int size);

    PedidoResponse marcarPedidoComoListo(Integer idPedido);
    PedidoResponse asignarseAPedido(Integer idPedido);

    // HU-15: Marcar pedido como ENTREGADO validando el PIN de seguridad
    PedidoResponse entregarPedido(Integer idPedido, EntregarPedidoRequest request);

    // HU-16: Cancelar pedido (solo el cliente dueño y en estado PENDIENTE)
    PedidoResponse cancelarPedido(Integer idPedido);

    // HU-17: Consultar el historial de un pedido
    List<TrazabilidadPedidoResponse> consultarTrazabilidadPedido(Integer idPedido);
}