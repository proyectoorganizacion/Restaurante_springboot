package com.restaurante.restaurante_spring.service.impl;


import com.restaurante.restaurante_spring.dto.request.CategoriaRequest;
import com.restaurante.restaurante_spring.dto.response.CategoriaResponse;
import com.restaurante.restaurante_spring.entity.Categoria;
import com.restaurante.restaurante_spring.repository.CategoriaRepository;
import com.restaurante.restaurante_spring.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaServiceImpl implements CategoriaService {
 private final CategoriaRepository categoriaRepository;

    @Override
    public CategoriaResponse registrarCategoria(CategoriaRequest Categoriarequest) {
        Categoria categoria = Categoria.builder()
                .nombre(Categoriarequest.getNombre())
                .descripcion(Categoriarequest.getDescripcion())
                .build();
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return CategoriaResponse.builder()
                .nombre(categoriaGuardada.getNombre())
                .descripcion(categoriaGuardada.getDescripcion())
                .build();
    }

    @Override
    public ArrayList<CategoriaResponse> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoria -> CategoriaResponse.builder()
                        .nombre(categoria.getNombre())
                        .descripcion(categoria.getDescripcion())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
