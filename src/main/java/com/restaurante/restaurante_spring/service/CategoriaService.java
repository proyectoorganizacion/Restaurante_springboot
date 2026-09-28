package com.restaurante.restaurante_spring.service;
import com.restaurante.restaurante_spring.dto.request.CategoriaRequest;
import com.restaurante.restaurante_spring.dto.response.CategoriaResponse;

import java.util.ArrayList;

public interface CategoriaService {
    CategoriaResponse registrarCategoria(CategoriaRequest Categoriarequest);
    ArrayList<CategoriaResponse> listarCategorias();
}
