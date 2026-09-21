package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.RestauranteRegisterRequest;
import com.restaurante.restaurante_spring.service.RestauranteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}