package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.AdminRegisterRequest;
import com.restaurante.restaurante_spring.dto.request.LoginRequest;
import com.restaurante.restaurante_spring.dto.response.LoginResponse;
import com.restaurante.restaurante_spring.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/crear-admin")
    public ResponseEntity<String> crearAdministrador(
            @Valid @RequestBody AdminRegisterRequest request) {

        authService.registrarAdministrador(request);

        return ResponseEntity.status(201)
                .body("Administrador creado correctamente");
    }

}
