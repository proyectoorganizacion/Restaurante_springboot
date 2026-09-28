package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoUpdateRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.entity.Plato;
import com.restaurante.restaurante_spring.repository.PlatoRepository;
import com.restaurante.restaurante_spring.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;

    @Override
    public PlatoResponse crearPlato(PlatoRequest request, String correoUsuarioAutenticado) {
        // 1. Validar regla de negocio básica (ej. precio positivo)
        if (request.getPrecio() == null || request.getPrecio() <= 0) {
            throw new RuntimeException("El precio del plato debe ser mayor a cero.");
        }

        // 2. Construir y guardar la entidad Plato
        Plato plato = Plato.builder()
                .nombre(request.getNombre())
                .precio(request.getPrecio())
                .descripcion(request.getDescripcion())
                .urlImagen(request.getUrlImagen())
                .activo(true)
                .build();

        Plato platoGuardado = platoRepository.save(plato);

        // 3. Mapear y retornar el PlatoResponse
        return PlatoResponse.builder()
                .id(platoGuardado.getId())
                .nombre(platoGuardado.getNombre())
                .precio(platoGuardado.getPrecio())
                .descripcion(platoGuardado.getDescripcion())
                .urlImagen(platoGuardado.getUrlImagen())
                .activo(platoGuardado.getActivo())
                .categoria(platoGuardado.getCategoria())
                .build();
    }

    @Override
    public PlatoResponse modificarPlato(Long idPlato, PlatoUpdateRequest request, String correoUsuarioAutenticado) {
        // 1. Buscar el plato existente (usando RuntimeException genérica por ahora para evitar bloqueos)
        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new RuntimeException("El plato con ID " + idPlato + " no existe."));

        // 2. Actualizar campos si vienen en el request
        if (request.getPrecio() != null) {
            if (request.getPrecio() <= 0) {
                throw new RuntimeException("El nuevo precio debe ser mayor a cero.");
            }
            plato.setPrecio(request.getPrecio());
        }

        if (request.getDescripcion() != null && !request.getDescripcion().isBlank()) {
            plato.setDescripcion(request.getDescripcion());
        }

        Plato platoActualizado = platoRepository.save(plato);

        // 3. Retornar el DTO de respuesta
        return PlatoResponse.builder()
                .id(platoActualizado.getId())
                .nombre(platoActualizado.getNombre())
                .precio(platoActualizado.getPrecio())
                .descripcion(platoActualizado.getDescripcion())
                .urlImagen(platoActualizado.getUrlImagen())
                .activo(platoActualizado.getActivo())
                .categoria(platoActualizado.getCategoria())
                .build();
    }
}