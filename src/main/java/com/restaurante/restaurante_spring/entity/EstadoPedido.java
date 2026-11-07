package com.restaurante.restaurante_spring.entity;

import java.util.Set;

/**
 * Patrón de diseño State (versión con enum).
 *
 * Cada estado del pedido "sabe" a qué estados puede pasar.
 * Así las reglas de transición viven en UN solo lugar
 * y no repartidas en if por todo el service.
 *
 * Flujo válido:
 *   PENDIENTE ──> EN_PREPARACION ──> LISTO ──> ENTREGADO
 *       │
 *       └──> CANCELADO
 *
 * ENTREGADO y CANCELADO son estados finales: no pueden cambiar.
 */
public enum EstadoPedido {

    PENDIENTE {
        @Override
        public Set<EstadoPedido> siguientesPermitidos() {
            return Set.of(EN_PREPARACION, CANCELADO); // HU-13 y HU-16
        }
    },

    EN_PREPARACION {
        @Override
        public Set<EstadoPedido> siguientesPermitidos() {
            return Set.of(LISTO); // HU-14
        }
    },

    LISTO {
        @Override
        public Set<EstadoPedido> siguientesPermitidos() {
            return Set.of(ENTREGADO); // HU-15
        }
    },

    ENTREGADO {
        @Override
        public Set<EstadoPedido> siguientesPermitidos() {
            return Set.of(); // Estado final (HU-15, criterio 2)
        }
    },

    CANCELADO {
        @Override
        public Set<EstadoPedido> siguientesPermitidos() {
            return Set.of(); // Estado final
        }
    };

    /** Cada estado define obligatoriamente a qué estados puede pasar. */
    public abstract Set<EstadoPedido> siguientesPermitidos();

    /** ¿Se permite pasar de este estado al nuevo? */
    public boolean puedeCambiarA(EstadoPedido nuevoEstado) {
        return siguientesPermitidos().contains(nuevoEstado);
    }

    /** Un estado es final si ya no puede pasar a ningún otro. */
    public boolean esFinal() {
        return siguientesPermitidos().isEmpty();
    }

    /**
     * Convierte el texto guardado en la BD (columna VARCHAR) al enum.
     * Pedido.estado sigue siendo String para no afectar las HU de los compañeros.
     */
    public static EstadoPedido desdeTexto(String estado) {
        if (estado == null) {
            throw new RuntimeException("El pedido no tiene un estado asignado");
        }
        try {
            return EstadoPedido.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado de pedido desconocido: " + estado);
        }
    }
}
