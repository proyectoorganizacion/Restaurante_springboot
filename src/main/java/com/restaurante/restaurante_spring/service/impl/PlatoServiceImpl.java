package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.ModificarPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PlatoRequest;
import com.restaurante.restaurante_spring.dto.response.PlatoResponse;
import com.restaurante.restaurante_spring.entity.Categoria;
import com.restaurante.restaurante_spring.entity.Plato;
import com.restaurante.restaurante_spring.entity.Restaurante;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.CategoriaRepository;
import com.restaurante.restaurante_spring.repository.PlatoRepository;
import com.restaurante.restaurante_spring.repository.RestauranteRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.service.PlatoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlatoServiceImpl implements PlatoService {

    private final PlatoRepository platoRepository;
    private final CategoriaRepository categoriaRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PlatoResponse registrarPlato(PlatoRequest platoRequest) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String correo = authentication.getName();

        Usuario propietario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El usuario autenticado no existe"));

        if (propietario.getRol() == null ||
                !"PROPIETARIO".equalsIgnoreCase(
                        propietario.getRol().getNombre())) {

            throw new RuntimeException(
                    "El usuario no tiene rol de PROPIETARIO");
        }

        Restaurante restaurante = restauranteRepository
                .findById(platoRequest.getIdRestaurante())
                .orElseThrow(() ->
                        new RuntimeException(
                                "El restaurante no existe"));

        if (!restaurante.getId_propietario()
                .equals(propietario.getId())) {

            throw new RuntimeException(
                    "El restaurante no pertenece al propietario autenticado");
        }

        Categoria categoria = categoriaRepository
                .findById(platoRequest.getCategoria())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Categoria no encontrada"));

        Plato plato = Plato.builder()
                .nombre(platoRequest.getNombre())
                .descripcion(platoRequest.getDescripcion())
                .urlImagen(platoRequest.getUrlImagen())
                .precio(platoRequest.getPrecio())
                .estado(platoRequest.getEstado())
                .categoria(categoria)
                .restaurante(restaurante)
                .build();

        Plato platoGuardado =
                platoRepository.save(plato);

        return PlatoResponse.builder()
                .nombre(platoGuardado.getNombre())
                .descripcion(platoGuardado.getDescripcion())
                .urlImagen(platoGuardado.getUrlImagen())
                .precio(platoGuardado.getPrecio())
                .estado(platoGuardado.getEstado())
                .categoria(categoria)
                .mensaje("Plato registrado correctamente")
                .build();
    }

    @Override
    public PlatoResponse modificarPlato(
            Integer idPlato,
            ModificarPlatoRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String correo = authentication.getName();

        Usuario propietario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El usuario autenticado no existe"));

        if (propietario.getRol() == null ||
                !"PROPIETARIO".equalsIgnoreCase(
                        propietario.getRol().getNombre())) {

            throw new RuntimeException(
                    "El usuario no tiene rol de PROPIETARIO");
        }

        Plato plato = platoRepository.findById(idPlato)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El plato no existe"));

        Restaurante restaurante = plato.getRestaurante();

        if (!restaurante.getId_propietario()
                .equals(propietario.getId())) {

            throw new RuntimeException(
                    "No tienes permisos para modificar este plato");
        }

        plato.setPrecio(request.getPrecio());
        plato.setDescripcion(request.getDescripcion());

        Plato platoActualizado =
                platoRepository.save(plato);

        return PlatoResponse.builder()
                .nombre(platoActualizado.getNombre())
                .descripcion(platoActualizado.getDescripcion())
                .precio(platoActualizado.getPrecio())
                .urlImagen(platoActualizado.getUrlImagen())
                .estado(platoActualizado.getEstado())
                .categoria(platoActualizado.getCategoria())
                .mensaje("Plato modificado correctamente")
                .build();
    }

}