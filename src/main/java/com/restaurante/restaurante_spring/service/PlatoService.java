package com.restaurante.restaurante_spring.service;

import com.restaurante.restaurante_spring.dto.request.CambiarEstadoPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.ModificarPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import org.springframework.data.domain.Page;



public interface PlatoService {
    PlatoResponse registrarPlato(PlatoRequest platoRequest);

    PlatoResponse modificarPlato(
            Integer idPlato,
            ModificarPlatoRequest request
    );

    PlatoResponse cambiarEstadoPlato(
            Integer idPlato,
            CambiarEstadoPlatoRequest request
    );
    // HU-10: Listar platos de un restaurante (con categoría opcional)
    Page<PlatoResponse> listarPlatosPorRestaurante(Integer idRestaurante, Integer idCategoria, int pagina, int tamaño);
}
