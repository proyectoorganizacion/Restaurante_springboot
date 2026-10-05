package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.PedidoRequest;
import com.restaurante.restaurante_spring.dto.response.PedidoResponse;
import com.restaurante.restaurante_spring.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // HU-11: Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<PedidoResponse> crearPedido(@Valid @RequestBody PedidoRequest request) {
        PedidoResponse response = pedidoService.crearPedido(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // HU-12: Obtener lista de pedidos filtrando por estado y paginado
    @GetMapping
    public ResponseEntity<Page<PedidoResponse>> obtenerPedidosPorEstado(
            @RequestParam(defaultValue = "PENDIENTE") String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<PedidoResponse> response = pedidoService.listarPedidosPorEstado(estado, page, size);
        return ResponseEntity.ok(response);
    }
}