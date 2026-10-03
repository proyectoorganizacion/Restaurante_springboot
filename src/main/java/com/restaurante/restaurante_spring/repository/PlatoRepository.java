package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.restaurante.restaurante_spring.entity.Plato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlatoRepository extends JpaRepository<Plato, Integer> {
   // PlatoResponse registrarPlato(PlatoResponse platoResponse);
// HU-10: Listar todos los platos de un restaurante paginados
   Page<Plato> findByRestauranteId(Integer idRestaurante, Pageable pageable);

    // HU-10: Listar los platos de un restaurante filtrados por categoría y paginados
    Page<Plato> findByRestauranteIdAndCategoriaId(Integer idRestaurante, Integer idCategoria, Pageable pageable);
}
