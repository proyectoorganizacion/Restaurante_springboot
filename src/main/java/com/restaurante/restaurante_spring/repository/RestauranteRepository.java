package com.restaurante.restaurante_spring.repository;

import com.restaurante.restaurante_spring.entity.Restaurante;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RestauranteRepository extends JpaRepository<Restaurante, Integer> {

    boolean existsByNit(String nit);

    @Query("SELECT r FROM Restaurante r WHERE r.id_propietario = :idPropietario")
    Optional<Restaurante> findByIdPropietario(
            @Param("idPropietario") Integer idPropietario
    );

    // línea nueva para la HU-9
    Page<Restaurante> findAllByOrderByNombreAsc(Pageable pageable);
}