package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.AdminRegisterRequest;
import com.restaurante.restaurante_spring.dto.request.LoginRequest;
import com.restaurante.restaurante_spring.dto.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);

    void registrarAdministrador(AdminRegisterRequest request);
}
