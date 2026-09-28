package com.restaurante.restaurante_spring.service.impl;


import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.entity.Categoria;
import com.restaurante.restaurante_spring.entity.Plato;
import com.restaurante.restaurante_spring.repository.CategoriaRepository;
import com.restaurante.restaurante_spring.repository.PlatoRepository;
import com.restaurante.restaurante_spring.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {
    private final PlatoRepository platoRepository;
    private final CategoriaRepository categoriaRepository;

    @Override
    public PlatoResponse registrarPlato(PlatoRequest platoRequest) {
        Categoria categoria = categoriaRepository.findById(platoRequest.getCategoria())
                .orElseThrow(()-> new RuntimeException("Categoria no encontrada"));

        Plato plato = Plato.builder()
                .nombre(platoRequest.getNombre())
                .descripcion(platoRequest.getDescripcion())
                .urlImagen(platoRequest.getUrlImagen())
                .precio(platoRequest.getPrecio())
                .estado(platoRequest.getEstado())
                .categoria(categoria)
                .build();

        Plato platoGuardado = platoRepository.save(plato);

        return PlatoResponse.builder()
                .nombre(platoGuardado.getNombre())
                .descripcion(platoGuardado.getDescripcion())
                .urlImagen(platoGuardado.getUrlImagen())
                .precio(platoGuardado.getPrecio())
                .estado(platoGuardado.getEstado())
                .categoria(categoria)
                .build();
    }
}
