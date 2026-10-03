package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.RestauranteRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.RestauranteListResponse;
import org.springframework.data.domain.Page;

public interface RestauranteService {
    void crearRestaurante(RestauranteRegisterRequest request);

    // Nuevo método agregado para la HU-9
    Page<RestauranteListResponse> listarRestaurantesPaginados(int pagina, int tamaño);

}