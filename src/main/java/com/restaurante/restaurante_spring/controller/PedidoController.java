package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.EntregarPedidoRequest;
import com.restaurante.restaurante_spring.dto.request.PedidoRequest;
import com.restaurante.restaurante_spring.dto.response.PedidoResponse;
import com.restaurante.restaurante_spring.dto.response.TrazabilidadPedidoResponse;
import com.restaurante.restaurante_spring.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // HU-11: Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(
            @Valid @RequestBody PedidoRequest request) {

        PedidoResponse response = pedidoService.crearPedido(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // HU-12: Obtener pedidos filtrados por estado y paginados
    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> obtenerPedidosPorEstado(
            @RequestParam(defaultValue = "PENDIENTE") String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PedidoResponse> response =
                pedidoService.listarPedidosPorEstado(estado, page, size);

        return ResponseEntity.ok(response);
    }

    // HU-13: Asignarse a un pedido y cambiarlo a EN_PREPARACION
    @PutMapping("/{id}/en-preparacion")
    public ResponseEntity<PedidoResponse> asignarseAPedido(
            @PathVariable Integer id) {

        PedidoResponse response =
                pedidoService.asignarseAPedido(id);

        return ResponseEntity.ok(response);
    }

    // HU-14: Marcar pedido como LISTO y notificar al cliente
    @PutMapping("/{id}/listo")
    public ResponseEntity<PedidoResponse> marcarPedidoComoListo(
            @PathVariable Integer id) {

        PedidoResponse response =
                pedidoService.marcarPedidoComoListo(id);

        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "HU-15: Entregar pedido",
            description = "El EMPLEADO marca el pedido como ENTREGADO digitando el PIN de 4 dígitos que recibió el cliente. "
                    + "Solo aplica a pedidos en estado LISTO del restaurante donde trabaja el empleado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido entregado"),
            @ApiResponse(responseCode = "400", description = "PIN incorrecto o inválido, pedido no LISTO o ya ENTREGADO, empleado de otro restaurante, pedido inexistente o rol distinto de EMPLEADO"),
            @ApiResponse(responseCode = "401", description = "Falta el token JWT o es inválido")
    })
    // HU-15: Entregar pedido (marcarlo como ENTREGADO) con el PIN de seguridad
    @PutMapping("/{id}/entregado")
    public ResponseEntity<PedidoResponse> entregarPedido(
            @PathVariable Integer id,
            @Valid @RequestBody EntregarPedidoRequest request) {

        PedidoResponse response =
                pedidoService.entregarPedido(id, request);

        return ResponseEntity.ok(response);
    }

    // HU-16: Cancelar pedido (solo el cliente dueño y en estado PENDIENTE)
    @Operation(
            summary = "HU-16: Cancelar pedido",
            description = "El CLIENTE cancela su propio pedido. Solo se permite si está en estado PENDIENTE.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado"),
            @ApiResponse(responseCode = "400", description = "No está PENDIENTE (\"Lo sentimos, tu pedido ya está en preparación y no puede cancelarse\"), ya fue cancelado, no es del cliente, no existe o el rol no es CLIENTE"),
            @ApiResponse(responseCode = "401", description = "Falta el token JWT o es inválido")
    })
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(
            @PathVariable Integer id) {

        PedidoResponse response =
                pedidoService.cancelarPedido(id);

        return ResponseEntity.ok(response);
    }

    // HU-17: Consultar la trazabilidad de un pedido
    @GetMapping("/{id}/trazabilidad")
    public ResponseEntity<List<TrazabilidadPedidoResponse>> consultarTrazabilidadPedido(
            @PathVariable Integer id) {

        List<TrazabilidadPedidoResponse> historial =
                pedidoService.consultarTrazabilidadPedido(id);

        return ResponseEntity.ok(historial);
    }
}