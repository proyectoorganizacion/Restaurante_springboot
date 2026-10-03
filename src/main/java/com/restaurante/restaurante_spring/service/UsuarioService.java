package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.UsuarioRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.UsuarioRegisterResponse;
import com.restaurante.restaurante_spring.dto.request.EmpleadoRegisterRequest;

public interface UsuarioService {
    UsuarioRegisterResponse registrarUsuario(
            UsuarioRegisterRequest request
    );

    UsuarioRegisterResponse registrarEmpleado(
            EmpleadoRegisterRequest request);
}
