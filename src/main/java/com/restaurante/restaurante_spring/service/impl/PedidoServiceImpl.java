package com.restaurante.restaurante_spring.service.impl;

import com.restaurante.restaurante_spring.dto.request.PedidoPlatoRequest;
import com.restaurante.restaurante_spring.dto.request.EntregarPedidoRequest;
import com.restaurante.restaurante_spring.entity.EstadoPedido;
import com.restaurante.restaurante_spring.repository.EmpleadoRestauranteRepository;
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
import com.restaurante.restaurante_spring.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.restaurante.restaurante_spring.entity.TrazabilidadPedido;
import com.restaurante.restaurante_spring.repository.TrazabilidadPedidoRepository;
import com.restaurante.restaurante_spring.dto.response.TrazabilidadPedidoResponse;


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
    private final SmsService smsService;
    private final EmpleadoRestauranteRepository empleadoRestauranteRepository;
    private final TrazabilidadPedidoRepository trazabilidadPedidoRepository;

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

        // Registrar la creación del pedido en la trazabilidad
        registrarCambioEstado(
                pedidoGuardado,
                null,
                "PENDIENTE",
                cliente
        );

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
    // HU-14: Notificar que el pedido está listo
    @Override
    @Transactional
    public PedidoResponse marcarPedidoComoListo(Integer idPedido) {

        // 1. Obtener el empleado autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String correo = authentication.getName();

        Usuario empleado = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El usuario autenticado no existe"));

        // 2. Validar rol EMPLEADO
        if (empleado.getRol() == null
                || !"EMPLEADO".equalsIgnoreCase(
                empleado.getRol().getNombre())) {

            throw new RuntimeException(
                    "Solo los usuarios con rol EMPLEADO pueden marcar pedidos como listos");
        }

        // 3. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El pedido con ID " + idPedido + " no existe"));

        // 4. Validar estado
        if (!"EN_PREPARACION".equalsIgnoreCase(pedido.getEstado())) {
            throw new RuntimeException(
                    "Solo se pueden pasar a listo pedidos que estén en estado EN_PREPARACION");
        }

        // 5. Generar PIN de entrega
        String pinGenerado =
                String.format("%04d", (int) (Math.random() * 10000));

        pedido.setPinEntrega(pinGenerado);

        // 6. Guardar el estado anterior
        String estadoAnterior = pedido.getEstado();

        // 7. Cambiar estado a LISTO
        pedido.setEstado("LISTO");

        // 8. Registrar la trazabilidad
        registrarCambioEstado(
                pedido,
                estadoAnterior,
                pedido.getEstado(),
                empleado
        );

        // 9. Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 10. Notificar al cliente mediante Twilio
        String telefonoCliente = pedido.getCliente().getCelular();
        String mensajeSms = "sms_order_confirmation";

        smsService.enviarSms(telefonoCliente, mensajeSms);

        // 11. Retornar respuesta
        return PedidoResponse.builder()
                .idPedido(pedidoGuardado.getId())
                .fechaCreacion(pedidoGuardado.getFechaCreacion())
                .estado(pedidoGuardado.getEstado())
                .idEmpleadoAsignado(empleado.getId())
                .mensaje("Pedido marcado como LISTO y PIN generado")
                .build();
    }

    // HU-13: Asignarse a un pedido y cambiar estado a EN_PREPARACION
    @Override
    public PedidoResponse asignarseAPedido(Integer idPedido) {

        // 1. Obtener el empleado autenticado
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String correo = authentication.getName();

        Usuario empleado = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El usuario autenticado no existe"));

        // 2. Validar rol EMPLEADO
        if (empleado.getRol() == null
                || !"EMPLEADO".equalsIgnoreCase(
                empleado.getRol().getNombre())) {

            throw new RuntimeException(
                    "Solo los usuarios con rol EMPLEADO pueden asignarse pedidos");
        }

        // 3. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() ->
                        new RuntimeException(
                                "El pedido con ID " + idPedido + " no existe"));

        // 4. Validar que el pedido esté PENDIENTE
        if (!"PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
            throw new RuntimeException(
                    "Solo se pueden asignar pedidos que estén en estado PENDIENTE");
        }

        // 5. Guardar el estado anterior
        String estadoAnterior = pedido.getEstado();

        // 6. Asignar empleado y cambiar estado
        pedido.setEmpleadoAsignado(empleado);
        pedido.setEstado("EN_PREPARACION");

        // 7. Registrar la trazabilidad
        registrarCambioEstado(
                pedido,
                estadoAnterior,
                pedido.getEstado(),
                empleado
        );

        // 8. Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        // 9. Retornar respuesta
        return PedidoResponse.builder()
                .idPedido(pedidoGuardado.getId())
                .fechaCreacion(pedidoGuardado.getFechaCreacion())
                .estado(pedidoGuardado.getEstado())
                .idEmpleadoAsignado(empleado.getId())
                .mensaje("Pedido asignado exitosamente y cambiado a estado EN_PREPARACION")
                .build();
    }

    // HU-15: Entregar pedido (marcarlo como ENTREGADO) validando el PIN de seguridad
    @Override
    @Transactional
    public PedidoResponse entregarPedido(Integer idPedido, EntregarPedidoRequest request) {

        // 1. Obtener el empleado autenticado
        Usuario empleado = obtenerUsuarioAutenticado();

        // 2. Validar rol EMPLEADO
        if (empleado.getRol() == null
                || !"EMPLEADO".equalsIgnoreCase(empleado.getRol().getNombre())) {
            throw new RuntimeException(
                    "Solo los usuarios con rol EMPLEADO pueden entregar pedidos");
        }

        // 3. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException(
                        "El pedido con ID " + idPedido + " no existe"));

        // 4. Validar que el empleado trabaje en el restaurante del pedido
        boolean trabajaEnRestaurante = empleadoRestauranteRepository
                .existsByUsuarioIdAndRestauranteId(
                        empleado.getId(), pedido.getRestaurante().getId());

        if (!trabajaEnRestaurante) {
            throw new RuntimeException(
                    "No puedes entregar pedidos de un restaurante en el que no trabajas");
        }

        // 5. Validar la transición de estado (Patrón State)
        EstadoPedido estadoActual = EstadoPedido.desdeTexto(pedido.getEstado());

        // Criterio 2: un pedido ENTREGADO (o CANCELADO) ya no puede modificarse
        if (estadoActual.esFinal()) {
            throw new RuntimeException(
                    "El pedido ya se encuentra en estado " + estadoActual + " y no puede modificarse");
        }

        // Criterio 1: solo los pedidos LISTO pueden pasar a ENTREGADO
        if (!estadoActual.puedeCambiarA(EstadoPedido.ENTREGADO)) {
            throw new RuntimeException(
                    "Solo se pueden entregar pedidos en estado LISTO. Estado actual: " + estadoActual);
        }

        // 6. Criterio 3: validar el PIN de seguridad enviado al cliente
        if (pedido.getPinEntrega() == null
                || !pedido.getPinEntrega().equals(request.getPin())) {
            throw new RuntimeException("El PIN de seguridad es incorrecto");
        }

        // 7. Guardar el estado anterior
        String estadoAnterior = pedido.getEstado();

        // 8. Cambiar estado a ENTREGADO
        pedido.setEstado(EstadoPedido.ENTREGADO.name());

        // 9. Registrar la trazabilidad
        registrarCambioEstado(
                pedido,
                estadoAnterior,
                pedido.getEstado(),
                empleado
        );

        // 10. Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 11. Retornar respuesta
        return PedidoResponse.builder()
                .idPedido(pedidoGuardado.getId())
                .fechaCreacion(pedidoGuardado.getFechaCreacion())
                .estado(pedidoGuardado.getEstado())
                .idEmpleadoAsignado(empleado.getId())
                .mensaje("Pedido entregado exitosamente")
                .build();
    }

    // HU-16: Cancelar pedido (solo el cliente dueño y en estado PENDIENTE)
    @Override
    @Transactional
    public PedidoResponse cancelarPedido(Integer idPedido) {

        // 1. Obtener el cliente autenticado
        Usuario cliente = obtenerUsuarioAutenticado();

        // 2. Validar rol CLIENTE
        if (cliente.getRol() == null
                || !"CLIENTE".equalsIgnoreCase(cliente.getRol().getNombre())) {
            throw new RuntimeException(
                    "Solo los usuarios con rol CLIENTE pueden cancelar pedidos");
        }

        // 3. Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException(
                        "El pedido con ID " + idPedido + " no existe"));

        // 4. Validar que el pedido pertenezca al cliente autenticado
        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("No puedes cancelar un pedido que no te pertenece");
        }

        // 5. Validar la transición de estado (Patrón State)
        EstadoPedido estadoActual = EstadoPedido.desdeTexto(pedido.getEstado());

        if (estadoActual == EstadoPedido.CANCELADO) {
            throw new RuntimeException("El pedido ya fue cancelado");
        }

        // Criterios 1 y 2: solo PENDIENTE puede pasar a CANCELADO
        if (!estadoActual.puedeCambiarA(EstadoPedido.CANCELADO)) {
            throw new RuntimeException(
                    "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse");
        }

        // 6. Guardar el estado anterior
        String estadoAnterior = pedido.getEstado();

        // 7. Cambiar estado a CANCELADO
        pedido.setEstado(EstadoPedido.CANCELADO.name());

        // 8. Registrar la trazabilidad
        registrarCambioEstado(
                pedido,
                estadoAnterior,
                pedido.getEstado(),
                cliente
        );

        // 9. Guardar el pedido
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // 10. Retornar respuesta
        return PedidoResponse.builder()
                .idPedido(pedidoGuardado.getId())
                .fechaCreacion(pedidoGuardado.getFechaCreacion())
                .estado(pedidoGuardado.getEstado())
                .mensaje("Pedido cancelado exitosamente")
                .build();
    }

    // Método de apoyo (HU-15 y HU-16): obtiene el usuario autenticado a partir del token JWT
    private Usuario obtenerUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("El usuario autenticado no existe"));
    }

    private void registrarCambioEstado(
            Pedido pedido,
            String estadoAnterior,
            String estadoNuevo,
            Usuario usuarioCambio) {

        TrazabilidadPedido trazabilidad = TrazabilidadPedido.builder()
                .pedido(pedido)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(estadoNuevo)
                .fechaCambio(LocalDateTime.now())
                .usuarioCambio(usuarioCambio)
                .build();

        trazabilidadPedidoRepository.save(trazabilidad);
    }


    // HU-17: Consultar la trazabilidad de un pedido
    @Override
    @Transactional(readOnly = true)
    public List<TrazabilidadPedidoResponse> consultarTrazabilidadPedido(Integer idPedido) {

        Usuario cliente = obtenerUsuarioAutenticado();

        if (cliente.getRol() == null
                || !"CLIENTE".equalsIgnoreCase(cliente.getRol().getNombre())) {
            throw new RuntimeException(
                    "Solo los usuarios con rol CLIENTE pueden consultar la trazabilidad");
        }


        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException(
                        "El pedido con ID " + idPedido + " no existe"));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException(
                    "No puedes consultar la trazabilidad de un pedido que no te pertenece");
        }

        return trazabilidadPedidoRepository
                .findByPedido_IdAndPedido_Cliente_IdOrderByFechaCambioAsc(
                        idPedido, cliente.getId())
                .stream()
                .map(registro -> TrazabilidadPedidoResponse.builder()
                        .idPedido(registro.getPedido().getId())
                        .estadoAnterior(registro.getEstadoAnterior())
                        .estadoNuevo(registro.getEstadoNuevo())
                        .fechaCambio(registro.getFechaCambio())
                        .nombreUsuarioCambio(
                                registro.getUsuarioCambio().getNombre())
                        .build())
                .toList();
    }

}