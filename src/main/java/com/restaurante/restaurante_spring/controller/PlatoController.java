package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoUpdateRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.service.PlatoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/platos")
@RequiredArgsConstructor
public class PlatoController {

    private final PlatoService platoService;

    @PostMapping
    public ResponseEntity crearPlato(@Valid @RequestBody PlatoRequest request,
                                     @RequestParam String correoUsuario) {
        PlatoResponse platoCreado = platoService.crearPlato(request, correoUsuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(platoCreado);
    }

    @PutMapping("/{id}")
    public ResponseEntity modificarPlato(@PathVariable Long id,
                                         @Valid @RequestBody PlatoUpdateRequest request,
                                         @RequestParam String correoUsuario) {
        PlatoResponse platoActualizado = platoService.modificarPlato(id, request, correoUsuario);
        return ResponseEntity.ok(platoActualizado);
    }
}

