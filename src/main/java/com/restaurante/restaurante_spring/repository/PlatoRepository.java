package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatoRepository extends JpaRepository<Plato, Integer> {
   // PlatoResponse registrarPlato(PlatoResponse platoResponse);

}
