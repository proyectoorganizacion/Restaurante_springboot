package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.RestauranteRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.RestauranteListResponse;
import com.restaurante.restaurante_spring.entity.Restaurante;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.RestauranteRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.service.RestauranteService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void crearRestaurante(@NonNull RestauranteRegisterRequest request) {
        // 1. Validar que el usuario exista
        Usuario propietario = usuarioRepository.findById(request.getId_propietario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario especificado no existe."));

        // 2. Validar que el usuario tenga el rol de PROPIETARIO
        if (propietario.getRol() == null || !"PROPIETARIO".equalsIgnoreCase(propietario.getRol().getNombre())) {
            throw new IllegalArgumentException("El id suministrado no corresponde a un usuario con rol de propietario.");
        }

        // 3. Validar que el NIT no esté duplicado
        if (restauranteRepository.existsByNit(request.getNit())) {
            throw new IllegalArgumentException("Ya existe un restaurante registrado con el NIT: " + request.getNit());
        }

        // 4. Guardar entidad Restaurante
        Restaurante restaurante = new Restaurante();
        restaurante.setNombre(request.getNombre());
        restaurante.setNit(request.getNit());
        restaurante.setDireccion(request.getDireccion());
        restaurante.setTelefono(request.getTelefono());
        restaurante.setUrl_logo(request.getUrl_logo());
        restaurante.setId_propietario(request.getId_propietario());

        restauranteRepository.save(restaurante);
    } // <--- LO QUE SE ARREGLÓ: Faltaba esta llave de cierre.

    // El nuevo método para la HU-9 --->
    @Override
    public Page<RestauranteListResponse> listarRestaurantesPaginados(int pagina, int tamaño) {
        Pageable pageable = PageRequest.of(pagina, tamaño);
        Page<Restaurante> restaurantes = restauranteRepository.findAllByOrderByNombreAsc(pageable);

        return restaurantes.map(restaurante -> new RestauranteListResponse(
                restaurante.getNombre(),
                restaurante.getUrl_logo()
        ));
    }
}