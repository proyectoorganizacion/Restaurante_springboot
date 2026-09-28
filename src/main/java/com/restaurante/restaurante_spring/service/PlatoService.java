package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoUpdateRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;

public interface PlatoService {

    PlatoResponse crearPlato(PlatoRequest request, String correoUsuarioAutenticado);

    PlatoResponse modificarPlato(Long idPlato, PlatoUpdateRequest request, String correoUsuarioAutenticado);
}

