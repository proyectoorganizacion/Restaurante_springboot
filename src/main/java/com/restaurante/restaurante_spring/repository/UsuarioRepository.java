package com.restaurante.restaurante_spring.repository;


import com.restaurante.restaurante_spring.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    Optional<Usuario> findByDocIdentidad(String docIdentidad);

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByDocIdentidad(String docIdentidad);

    boolean existsByCorreo(String correo);
}
