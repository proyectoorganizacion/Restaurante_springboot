package com.restaurante.restaurante_spring.util;

import com.restaurante.restaurante_spring.entity.Restaurante;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

/**
 * Algoritmo de búsqueda binaria aplicado a restaurantes ordenados por nombre.
 *
 * Analogía: buscar una palabra en el diccionario. Se abre por la mitad,
 * se mira si la palabra va antes o después y se descarta la otra mitad.
 *
 * Complejidad: O(log n). Con 1.000 restaurantes hace como máximo ~10 comparaciones
 * para encontrar la posición (una búsqueda lineal haría hasta 1.000).
 *
 * REQUISITO: la lista debe estar ordenada con el mismo criterio de normalizar().
 */
public final class BusquedaBinaria {

    private BusquedaBinaria() {
        // Clase utilitaria: solo tiene métodos static, no se crean objetos de ella
    }

    /**
     * Devuelve los restaurantes cuyo nombre EMPIEZA por el texto buscado.
     * Ejemplo: "piz" encuentra "Pizza Express" y "Pizzería Roma".
     */
    public static List<Restaurante> buscarPorPrefijo(List<Restaurante> ordenados, String textoBuscado) {
        String prefijo = normalizar(textoBuscado);

        // 1. Búsqueda binaria: encontrar la PRIMERA posición cuyo nombre sea >= prefijo
        int izquierda = 0;
        int derecha = ordenados.size();

        while (izquierda < derecha) {
            // Se calcula así (y no (izquierda + derecha) / 2) para evitar desbordamiento de int
            int medio = izquierda + (derecha - izquierda) / 2;
            String nombreMedio = normalizar(ordenados.get(medio).getNombre());

            if (nombreMedio.compareTo(prefijo) < 0) {
                izquierda = medio + 1; // El buscado está a la derecha: descartamos la mitad izquierda
            } else {
                derecha = medio;       // El buscado está aquí o a la izquierda: descartamos la mitad derecha
            }
        }

        // 2. Como la lista está ordenada, todos los que empiezan por el prefijo están seguidos
        List<Restaurante> resultado = new ArrayList<>();
        for (int i = izquierda; i < ordenados.size(); i++) {
            String nombre = normalizar(ordenados.get(i).getNombre());
            if (!nombre.startsWith(prefijo)) {
                break; // El primero que no coincide marca el final
            }
            resultado.add(ordenados.get(i));
        }

        return resultado;
    }

    /**
     * Pasa a minúsculas y quita tildes: "Pizzería " -> "pizzeria".
     * Se usa tanto para ORDENAR como para BUSCAR, así ambos usan el mismo criterio.
     */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        String sinTildes = Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        return sinTildes.toLowerCase().trim();
    }
}

