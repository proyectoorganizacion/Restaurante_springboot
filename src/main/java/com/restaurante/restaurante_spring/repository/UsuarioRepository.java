package com.restaurante.restaurante_spring.repository;


import com.restaurante.restaurante_spring.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Optional<Usuario> findByIdentification(String identification);

    Optional<Usuario> findByEmail(String email);

    boolean existsByIdentification(String identification);

    boolean existsByEmail(String email);
}
