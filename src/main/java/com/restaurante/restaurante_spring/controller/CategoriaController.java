package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.CategoriaRequest;
import com.restaurante.restaurante_spring.dto.response.CategoriaResponse;
import com.restaurante.restaurante_spring.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@RequiredArgsConstructor
public class CategoriaController {
    private final CategoriaService categoriaService;

    @PostMapping("/registro")
    public ResponseEntity<CategoriaResponse> registrarCategoria(@RequestBody CategoriaRequest categoriaRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.registrarCategoria(categoriaRequest));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarCategorias() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(categoriaService.listarCategorias());
    }
}
