package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.LoginRequest;
import com.restaurante.restaurante_spring.dto.response.LoginResponse;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl {
    private final UsuarioRepository usuarioRepository;

    @Override
    public LoginResponse login


    Usuario usuario = usuarioRepository.findByDocIdentidad(LoginRequest.getDocIdentidad())
            .orElseThrow(() -> new RuntimeException("El usuario con documento " + docIdentidad + " no existe"));

    boolean passwordCorrecta = passwordEncoder.matches(LoginRequest.getPassword(), usuario.getPassword());
    if(!passwordCorrecta){
        throw new RuntimeException("Contraseña incorrecta");

        return null;
    }
}