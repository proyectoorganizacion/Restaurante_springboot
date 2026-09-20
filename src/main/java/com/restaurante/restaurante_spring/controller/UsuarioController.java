package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.UsuarioRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.UsuarioRegisterResponse;
import com.restaurante.restaurante_spring.service.UsuarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/registro")
    public ResponseEntity<UsuarioRegisterResponse> registrarUsuario(
            @Valid @RequestBody UsuarioRegisterRequest request) {

        UsuarioRegisterResponse response =
                usuarioService.registrarUsuario(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}