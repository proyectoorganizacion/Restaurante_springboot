package com.restaurante.restaurante_spring.controller;

import com.restaurante.restaurante_spring.dto.request.RestauranteRegisterRequest;
import com.restaurante.restaurante_spring.dto.response.RestauranteListResponse;
import com.restaurante.restaurante_spring.service.RestauranteService;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/restaurante")
public class RestauranteController {

    @Autowired
    private RestauranteService restauranteService;

    @PostMapping
    public ResponseEntity<String> crearRestaurante(@Valid @RequestBody RestauranteRegisterRequest request) {
        restauranteService.crearRestaurante(request);
        return new ResponseEntity<>("Restaurante creado exitosamente", HttpStatus.CREATED);
    }

    // Endpoint agregado para la HU-9
    @Operation(
            summary = "HU-9: Listar restaurantes",
            description = "Lista paginada de restaurantes ordenada alfabéticamente por nombre. Devuelve nombre y logo.")
    @GetMapping("/listar")
    public ResponseEntity<Page<RestauranteListResponse>> listarRestaurantes(
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamaño) {

        Page<RestauranteListResponse> respuesta = restauranteService.listarRestaurantesPaginados(pagina, tamaño);
        return ResponseEntity.ok(respuesta);
    }


    // Algoritmo de búsqueda: búsqueda binaria de restaurantes por nombre (prefijo)
    @Operation(
            summary = "Buscar restaurantes por nombre (búsqueda binaria)",
            description = "Devuelve los restaurantes cuyo nombre EMPIEZA por el texto enviado, sin distinguir "
                    + "mayúsculas ni tildes. Ejemplo: nombre=piz encuentra 'Pizzería Roma'. Usa búsqueda binaria O(log n).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de coincidencias (vacía si no hay ninguna)"),
            @ApiResponse(responseCode = "400", description = "El parámetro nombre está vacío"),
            @ApiResponse(responseCode = "401", description = "Falta el token JWT o es inválido")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<RestauranteListResponse>> buscarRestaurantes(
            @RequestParam String nombre) {

        List<RestauranteListResponse> respuesta = restauranteService.buscarRestaurantesPorNombre(nombre);
        return ResponseEntity.ok(respuesta);
    }
}