package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.entity.EmpleadoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRestauranteRepository
        extends JpaRepository<EmpleadoRestaurante, Integer> {
}