package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.PlatoEstadoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoUpdateRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.entity.Plato;
import com.restaurante.restaurante_spring.entity.Restaurante;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.PlatoRepository;
import com.restaurante.restaurante_spring.repository.RestauranteRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PlatoResponse crearPlato(PlatoRequest request, String correoUsuarioAutenticado) {
        // 1. Validar que el usuario exista
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuarioAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con el correo proporcionado."));

        // 2. Validar que el usuario sea Propietario
        if (usuario.getRol() == null || !"PROPIETARIO".equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new RuntimeException("No tienes permisos de propietario para crear platos.");
        }

        // 3. Buscar y asociar el restaurante
        Restaurante restaurante = restauranteRepository.findById(request.getIdRestaurante())
                .orElseThrow(() -> new RuntimeException("Restaurante no encontrado."));

        // 4. Validar precio positivo
        if (request.getPrecio() == null || request.getPrecio() <= 0) {
            throw new RuntimeException("El precio del plato debe ser mayor a cero.");
        }

        // 5. Construir y guardar la entidad Plato con su restaurante
        Plato plato = Plato.builder()
                .nombre(request.getNombre())
                .precio(request.getPrecio())
                .descripcion(request.getDescripcion())
                .urlImagen(request.getUrlImagen())
                .activo(true)
                .restaurante(restaurante)
                .build();

        Plato platoGuardado = platoRepository.save(plato);

        return mapearADto(platoGuardado);
    }

    @Override
    public PlatoResponse modificarPlato(Long idPlato, PlatoUpdateRequest request, String correoUsuarioAutenticado) {
        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new RuntimeException("El plato con ID " + idPlato + " no existe."));

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

        return mapearADto(platoActualizado);
    }

    @Override
    public PlatoResponse cambiarEstadoPlato(Long idPlato, PlatoEstadoRequest request, String correoUsuarioAutenticado) {
        // 1. Validar usuario y rol de Propietario
        Usuario usuario = usuarioRepository.findByCorreo(correoUsuarioAutenticado)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado."));

        if (usuario.getRol() == null || !"PROPIETARIO".equalsIgnoreCase(usuario.getRol().getNombre())) {
            throw new RuntimeException("Solo el propietario puede habilitar/deshabilitar platos.");
        }

        // 2. Buscar el plato
        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() -> new RuntimeException("Plato no encontrado."));

        // 3. Validar que el plato tenga restaurante asignado
        if (plato.getRestaurante() == null) {
            throw new RuntimeException("El plato no tiene un restaurante asociado.");
        }

        // 4. Actualizar estado y guardar
        plato.setActivo(request.getActivo());
        Plato platoActualizado = platoRepository.save(plato);

        return mapearADto(platoActualizado);
    }

    // Método auxiliar para el mapeo a Response (con conversión segura de Integer a Long)
    private PlatoResponse mapearADto(Plato plato) {
        return PlatoResponse.builder()
                .id(plato.getId())
                .nombre(plato.getNombre())
                .precio(plato.getPrecio())
                .descripcion(plato.getDescripcion())
                .urlImagen(plato.getUrlImagen())
                .activo(plato.getActivo())
                .categoria(plato.getCategoria())
                .idRestaurante(plato.getRestaurante() != null && plato.getRestaurante().getId() != null
                        ? plato.getRestaurante().getId().longValue()
                        : null)
                .build();
    }
}