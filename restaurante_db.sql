DROP DATABASE IF EXISTS restaurante_db;
CREATE DATABASE restaurante_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restaurante_db;

-- 1. Tabla de Roles (Propietario, Empleado, Cliente, Administrador)
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255)
);

-- 2. Tabla de Usuarios (Propietarios, Empleados, Clientes, Admins)
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    documento_identidad VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    clave VARCHAR(255) NOT NULL,
    rol_id BIGINT NOT NULL,
    CONSTRAINT fk_usuarios_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
);

-- 3. Tabla de Restaurantes
CREATE TABLE restaurantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(50) NOT NULL UNIQUE,
    direccion VARCHAR(255) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    url_logo VARCHAR(255),
    nit_propietario BIGINT NOT NULL
);

-- 4. Tabla de Categorías (para los platos)
CREATE TABLE categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- 5. Tabla de Platos (HU 3, HU 4, HU 7, HU 10)
CREATE TABLE platos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DOUBLE NOT NULL,
    descripcion TEXT,
    url_imagen VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    categoria_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_platos_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    CONSTRAINT fk_platos_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

-- 6. Tabla intermedia para asignar Empleados a Restaurantes (HU 6)
CREATE TABLE empleado_restaurante (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_empleado_restaurante_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    CONSTRAINT fk_empleado_restaurante_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

-- 7. Tabla de Pedidos (HU 11, HU 12)
CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_creacion DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL, -- PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO
    pin_entrega VARCHAR(10),
    cliente_id BIGINT NOT NULL,
    empleado_asignado_id BIGINT,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_pedidos_cliente FOREIGN KEY (cliente_id) REFERENCES usuarios(id),
    CONSTRAINT fk_pedidos_empleado FOREIGN KEY (empleado_asignado_id) REFERENCES usuarios(id),
    CONSTRAINT fk_pedidos_restaurante FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
);

-- 8. Tabla de Detalles del Pedido (Platos asociados a un pedido)
CREATE TABLE detalle_pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DOUBLE NOT NULL,
    pedido_id BIGINT NOT NULL,
    plato_id BIGINT NOT NULL,
    CONSTRAINT fk_detalle_pedidos_pedido FOREIGN KEY (pedido_id) REFERENCES pedidos(id),
    CONSTRAINT fk_detalle_pedidos_plato FOREIGN KEY (plato_id) REFERENCES platos(id)
);

-- 9. Tabla de Trazabilidad de Pedidos (Historial de cambios de estado)
CREATE TABLE trazabilidad_pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    fecha_cambio DATETIME NOT NULL,
    usuario_cambio_id BIGINT NOT NULL,
    CONSTRAINT fk_trazabilidad_pedidos_pedido FOREIGN KEY (pedido_id)
        REFERENCES pedidos (id),
    CONSTRAINT fk_trazabilidad_pedidos_usuario FOREIGN KEY (usuario_cambio_id)
        REFERENCES usuarios (id)
);