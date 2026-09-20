package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.UsuarioRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.UsuarioRegisterResponse;

public interface UsuarioService {
    UsuarioRegisterResponse registrarUsuario(
            UsuarioRegisterRequest request
    );
}
