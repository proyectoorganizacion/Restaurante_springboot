package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.entity.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Integer> {
}