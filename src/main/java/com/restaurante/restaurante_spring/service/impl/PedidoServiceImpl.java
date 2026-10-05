package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.PedidoPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.PedidoRequest;
import com.restaurante.restaurante_spring.dto.response.PedidoResponse;
import com.restaurante.restaurante_spring.entity.DetallePedido;
import com.restaurante.restaurante_spring.entity.Pedido;
import com.restaurante.restaurante_spring.entity.Plato;
import com.restaurante.restaurante_spring.entity.Restaurante;
import com.restaurante.restaurante_spring.entity.Usuario;
import com.restaurante.restaurante_spring.repository.PedidoRepository;
import com.restaurante.restaurante_spring.repository.PlatoRepository;
import com.restaurante.restaurante_spring.repository.RestauranteRepository;
import com.restaurante.restaurante_spring.repository.UsuarioRepository;
import com.restaurante.restaurante_spring.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;
    private final RestauranteRepository restauranteRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    public PedidoResponse crearPedido(PedidoRequest request) {
        // 1. Obtener cliente autenticado desde el token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();
        Usuario cliente = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("El usuario autenticado no existe"));

        if (cliente.getRol() == null || !"CLIENTE".equalsIgnoreCase(cliente.getRol().getNombre())) {
            throw new RuntimeException("Solo los usuarios con rol CLIENTE pueden realizar pedidos");
        }

        // 2. Validar que el cliente no tenga un pedido en proceso (Criterio HU-11)
        List<String> estadosEnProceso = Arrays.asList("PENDIENTE", "EN_PREPARACION", "LISTO");
        if (pedidoRepository.existsByClienteIdAndEstadoIn(cliente.getId(), estadosEnProceso)) {
            throw new RuntimeException("No puedes crear un nuevo pedido porque ya tienes uno en proceso.");
        }

        // 3. Validar que el restaurante exista
        Restaurante restaurante = restauranteRepository.findById(request.getIdRestaurante())
                .orElseThrow(() -> new RuntimeException("El restaurante seleccionado no existe"));

        // 4. Iniciar la construcción del pedido
        Pedido pedido = Pedido.builder()
                .fechaCreacion(LocalDateTime.now())
                .estado("PENDIENTE") // Criterio HU-11: Estado inicial obligatorio
                .cliente(cliente)
                .restaurante(restaurante)
                .build();

        // 5. Validar los platos y armar el detalle
        List<DetallePedido> detalles = new ArrayList<>();
        for (PedidoPlatoRequest platoReq : request.getPlatos()) {
            Plato plato = platoRepository.findById(platoReq.getIdPlato())
                    .orElseThrow(() -> new RuntimeException("El plato con ID " + platoReq.getIdPlato() + " no existe"));

            // Criterio HU-11: Validar que todos los platos pertenezcan al mismo restaurante
            if (!plato.getRestaurante().getId().equals(restaurante.getId())) {
                throw new RuntimeException("El plato '" + plato.getNombre() + "' no pertenece al restaurante seleccionado.");
            }

            // Armar el detalle del pedido
            DetallePedido detalle = DetallePedido.builder()
                    .cantidad(platoReq.getCantidad())
                    .precioUnitario(plato.getPrecio()) // Tomamos el precio de la BD, no confiamos en el cliente
                    .pedido(pedido) // Amarramos el detalle al pedido padre
                    .plato(plato)
                    .build();

            detalles.add(detalle);
        }

        // 6. Asignar la lista de detalles al pedido general
        pedido.setDetalles(detalles);

        // 7. Guardar en BD. Al guardar 'pedido', el CascadeType.ALL guarda automáticamente los detalles.
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 8. Retornar respuesta exitosa
        return PedidoResponse.builder()
                .idPedido(pedidoGuardado.getId())
                .fechaCreacion(pedidoGuardado.getFechaCreacion())
                .estado(pedidoGuardado.getEstado())
                .mensaje("Pedido creado exitosamente y está " + pedidoGuardado.getEstado())
                .build();
    }
    // HU-12: Obtener pedidos filtrados por estado para el empleado autenticado
    @Override
    public Page<PedidoResponse> listarPedidosPorEstado(String estado, int page, int size) {
        // 1. Obtener usuario autenticado desde el token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();

        Usuario empleado = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("El usuario autenticado no existe"));

        // 2. Validar que el usuario tenga rol EMPLEADO
        if (empleado.getRol() == null || !"EMPLEADO".equalsIgnoreCase(empleado.getRol().getNombre())) {
            throw new RuntimeException("Solo los usuarios con rol EMPLEADO pueden ver la lista de pedidos");
        }

        // 3. Paginación y consulta
        Pageable pageable = PageRequest.of(page, size);
        Page<Pedido> pedidosPage = pedidoRepository.findByEmpleadoCorreoYEstado(correo, estado, pageable);

        // 4. Mapear cada entidad Pedido a PedidoResponse
        return pedidosPage.map(pedido -> PedidoResponse.builder()
                .idPedido(pedido.getId())
                .fechaCreacion(pedido.getFechaCreacion())
                .estado(pedido.getEstado())
                .mensaje("Pedido encontrado")
                .build());
    }
}