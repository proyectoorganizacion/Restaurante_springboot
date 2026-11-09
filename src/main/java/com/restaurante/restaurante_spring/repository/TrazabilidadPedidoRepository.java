
package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.entity.TrazabilidadPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrazabilidadPedidoRepository
        extends JpaRepository<TrazabilidadPedido, Integer> {

    List<TrazabilidadPedido> findByPedido_Cliente_IdOrderByFechaCambioAsc(
            Integer clienteId
    );

    // HU-17: Consultar el historial de un pedido específico de un cliente
    List<TrazabilidadPedido> findByPedido_IdAndPedido_Cliente_IdOrderByFechaCambioAsc(
            Integer pedidoId,
            Integer clienteId
    );
}
