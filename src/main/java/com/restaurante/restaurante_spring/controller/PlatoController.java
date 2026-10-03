package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.CambiarEstadoPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.ModificarPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/platos")
@RequiredArgsConstructor

public class PlatoController {
    private final PlatoService platoService;

    @PostMapping("/registro")
    public ResponseEntity<PlatoResponse> registrarPlato(@Valid @RequestBody PlatoRequest platoRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(platoService.registrarPlato(platoRequest));
    }

    @PutMapping("/{idPlato}")
    public ResponseEntity<PlatoResponse> modificarPlato(
            @PathVariable Integer idPlato,
            @Valid @RequestBody ModificarPlatoRequest request) {

        return ResponseEntity.ok(
                platoService.modificarPlato(idPlato, request)
        );
    }

    @PutMapping("/{idPlato}/estado")
    public ResponseEntity<PlatoResponse> cambiarEstadoPlato(
            @PathVariable Integer idPlato,
            @Valid @RequestBody CambiarEstadoPlatoRequest request) {

        return ResponseEntity.ok(
                platoService.cambiarEstadoPlato(idPlato, request)
        );
    }

    // <--- MÉTODO NUEVO PARA LA HU-10 --->
    @GetMapping("/restaurante/{idRestaurante}")
    public ResponseEntity<Page<PlatoResponse>> listarPlatosPorRestaurante(
            @PathVariable Integer idRestaurante,
            @RequestParam(required = false) Integer idCategoria,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño) {

        Page<PlatoResponse> respuesta = platoService.listarPlatosPorRestaurante(idRestaurante, idCategoria, pagina, tamaño);
        return ResponseEntity.ok(respuesta);
    }
}
