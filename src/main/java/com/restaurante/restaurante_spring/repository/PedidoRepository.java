package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.entity.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // HU-11: Valida automáticamente si el cliente ya tiene un pedido en los estados que le pasemos
    boolean existsByClienteIdAndEstadoIn(Integer idCliente, List<String> estados);

    // HU-12: Buscar pedidos por estado que pertenecen al restaurante asignado al empleado autenticado
    @Query("SELECT p FROM Pedido p " +
            "JOIN EmpleadoRestaurante er ON er.restaurante.id = p.restaurante.id " +
            "WHERE er.usuario.correo = :correoEmpleado AND p.estado = :estado")
    Page<Pedido> findByEmpleadoCorreoYEstado(
            @Param("correoEmpleado") String correoEmpleado,
            @Param("estado") String estado,
            Pageable pageable);
}