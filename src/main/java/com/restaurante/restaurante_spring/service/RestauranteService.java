package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.RestauranteRegisterRequest;

public interface RestauranteService {
    void crearRestaurante(RestauranteRegisterRequest request);
}