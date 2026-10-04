package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // HU-11: Valida automáticamente si el cliente ya tiene un pedido en los estados que le pasemos
    boolean existsByClienteIdAndEstadoIn(Integer idCliente, List<String> estados);
}