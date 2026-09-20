CREATE DATABASE IF NOT EXISTS restaurante_db;
USE restaurante_db;

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
    apellido VARCHAR(100) NOT NULL,
    doc_identidad VARCHAR(20) NOT NULL UNIQUE,
    celular VARCHAR(13),
    fecha_nacimiento DATE NOT NULL,
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
    direccion VARCHAR(100) NOT NULL,
    telefono VARCHAR(13),   
    url_logo VARCHAR(255),
    id_propietario INT NOT NULL,
    CONSTRAINT fk_restaurante_propietario FOREIGN KEY (id_propietario) REFERENCES usuario(id)
    );

-- 4. Tabla PLATO
CREATE TABLE IF NOT EXISTS plato (
                                     id INT AUTO_INCREMENT PRIMARY KEY,
                                     nombre VARCHAR(100) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    descripcion VARCHAR(255),
    urlImagen VARCHAR(255),
    categoria VARCHAR(100),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    id_restaurante INT NOT NULL,
    CONSTRAINT fk_plato_restaurante FOREIGN KEY (id_restaurante) REFERENCES restaurante(id)
    );

    select * from rol;
SELECT * FROM usuario;
drop DATABASE restaurante_db;
  