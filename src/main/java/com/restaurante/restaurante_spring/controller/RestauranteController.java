package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.RestauranteRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.RestauranteListResponse;
import com.restaurante.restaurante_spring.service.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurante")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<String> crearRestaurante(@Valid @RequestBody RestauranteRegisterRequest request) {
        restauranteService.crearRestaurante(request);
        return new ResponseEntity<>("Restaurante creado exitosamente", HttpStatus.CREATED);
    }

    // Endpoint agregado para la HU-9
    @GetMapping("/listar")
    public ResponseEntity<Page<RestauranteListResponse>> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño) {

        Page<RestauranteListResponse> respuesta = restauranteService.listarRestaurantesPaginados(pagina, tamaño);
        return ResponseEntity.ok(respuesta);
    }
}