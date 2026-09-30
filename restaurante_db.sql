DROP DATABASE IF EXISTS restaurante_db;
CREATE DATABASE IF NOT EXISTS restaurante_db;
USE restaurante_db;

-- ==========================================
-- 1. TABLAS BASE ORIGINALES Y AMPLIADAS
-- ==========================================

-- 1. Tabla ROL
CREATE TABLE IF NOT EXISTS rol (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

INSERT IGNORE INTO rol (id, nombre) VALUES
(1, 'ADMINISTRADOR'),
(2, 'PROPIETARIO'),
(3, 'EMPLEADO'),
(4, 'CLIENTE');

-- 2. Tabla USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    doc_identidad VARCHAR(20) NOT NULL UNIQUE,
    correo VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id)
);

-- 3. Tabla RESTAURANTE
CREATE TABLE IF NOT EXISTS restaurante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nit VARCHAR(20) NOT NULL UNIQUE,
    id_propietario INT NOT NULL,
    CONSTRAINT fk_restaurante_propietario FOREIGN KEY (id_propietario) REFERENCES usuario(id)
);

-- 4. Tabla CATEGORIA (Creada antes para poder referenciarla en plato)
CREATE TABLE IF NOT EXISTS categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- 5. Tabla PLATO (Extendida con descripción, imagen y categoría desde el inicio)
CREATE TABLE IF NOT EXISTS plato (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    descripcion TEXT,
    url_imagen VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    id_restaurante INT NOT NULL,
    id_categoria INT,
    CONSTRAINT fk_plato_restaurante FOREIGN KEY (id_restaurante) REFERENCES restaurante(id),
    CONSTRAINT fk_plato_categoria FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);


-- ==========================================
-- 2. EXTENSIÓN HASTA LA HU 12
-- ==========================================

-- 6. Tabla EMPLEADO_RESTAURANTE (HU 6 - Asignación de empleados a un restaurante)
CREATE TABLE IF NOT EXISTS empleado_restaurante (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_restaurante INT NOT NULL,
    CONSTRAINT fk_empleado_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id),
    CONSTRAINT fk_empleado_restaurante FOREIGN KEY (id_restaurante) REFERENCES restaurante(id)
);

-- 7. Tabla PEDIDO (HU 11 y HU 12 - Gestión de pedidos y estados)
CREATE TABLE IF NOT EXISTS pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha_creacion DATETIME NOT NULL,
    estado VARCHAR(50) NOT NULL, -- PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO
    pin_entrega VARCHAR(10),
    id_cliente INT NOT NULL,
    id_empleado_asignado INT,
    id_restaurante INT NOT NULL,
    CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente) REFERENCES usuario(id),
    CONSTRAINT fk_pedido_empleado FOREIGN KEY (id_empleado_asignado) REFERENCES usuario(id),
    CONSTRAINT fk_pedido_restaurante FOREIGN KEY (id_restaurante) REFERENCES restaurante(id)
);

-- 8. Tabla DETALLE_PEDIDO (Platos asociados a cada pedido con su cantidad)
CREATE TABLE IF NOT EXISTS detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10, 2) NOT NULL,
    id_pedido INT NOT NULL,
    id_plato INT NOT NULL,
    CONSTRAINT fk_detalle_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    CONSTRAINT fk_detalle_plato FOREIGN KEY (id_plato) REFERENCES plato(id)
);

-- 9. Tabla TRAZABILIDAD_PEDIDO (Historial y seguimiento de cambios de estado del pedido)
CREATE TABLE IF NOT EXISTS trazabilidad_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    estado_anterior VARCHAR(50),
    estado_nuevo VARCHAR(50) NOT NULL,
    fecha_cambio DATETIME NOT NULL,
    id_usuario_cambio INT NOT NULL,
    CONSTRAINT fk_trazabilidad_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    CONSTRAINT fk_trazabilidad_usuario FOREIGN KEY (id_usuario_cambio) REFERENCES usuario(id)
);