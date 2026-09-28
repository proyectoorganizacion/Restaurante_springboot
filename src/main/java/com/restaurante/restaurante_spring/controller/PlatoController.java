package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/platos")
@RequiredArgsConstructor

public class PlatoController {
    private final PlatoService platoService;

    @PostMapping("/registro")
    public ResponseEntity<PlatoResponse> registrarPlato(@RequestBody PlatoRequest platoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(platoService.registrarPlato(platoRequest));
    }
}
