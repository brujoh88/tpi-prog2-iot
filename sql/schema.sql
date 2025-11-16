-- =====================================================
-- TPI Programación 2 - Sistema de Gestión de IoT
-- Archivo: schema.sql
-- Descripción: Creación de base de datos y tablas
-- Autor: Mauricio López
-- Fecha: 2025-11
-- =====================================================

-- Crear base de datos
DROP DATABASE IF EXISTS iot;
CREATE DATABASE iot CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE iot;

-- =====================================================
-- Tabla: DispositivoIoT (Clase A)
-- =====================================================
CREATE TABLE DispositivoIoT (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    serial VARCHAR(50) NOT NULL UNIQUE,
    modelo VARCHAR(50) NOT NULL,
    ubicacion VARCHAR(120) NOT NULL,
    firmwareVersion VARCHAR(30) NULL,

    -- Índices para optimizar búsquedas
    INDEX idx_serial (serial),
    INDEX idx_eliminado (eliminado),
    INDEX idx_ubicacion (ubicacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Tabla: ConfiguracionRed (Clase B)
-- Relación 1→1 con DispositivoIoT mediante FK única
-- =====================================================
CREATE TABLE ConfiguracionRed (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    ip VARCHAR(45) UNIQUE,
    mascara VARCHAR(45),
    gateway VARCHAR(45),
    dnsPrimario VARCHAR(45),
    dhcpHabilitado BOOLEAN NOT NULL,
    dispositivo_id BIGINT NOT NULL UNIQUE,

    -- Foreign Key con CASCADE para mantener integridad referencial
    CONSTRAINT fk_configuracion_dispositivo
        FOREIGN KEY (dispositivo_id)
        REFERENCES DispositivoIoT (id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    -- Constraint para validar formato de IP (IPv4)
    CONSTRAINT chk_ip_formato
        CHECK (
            ip REGEXP '^([0-9]{1,3}\\.){3}[0-9]{1,3}$'
            OR ip IS NULL
        ),

    -- Índices para optimizar búsquedas
    INDEX idx_ip (ip),
    INDEX idx_eliminado (eliminado),
    INDEX idx_dispositivo_id (dispositivo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Vista: Inventario de Red Activo
-- Combina información de dispositivos y configuraciones
-- =====================================================
CREATE VIEW Vista_Inventario_Red_Activo AS
SELECT
    d.id AS id_dispositivo,
    d.serial,
    d.modelo,
    d.ubicacion,
    d.firmwareVersion,
    c.id AS id_configuracion,
    c.ip,
    c.mascara,
    c.gateway,
    c.dnsPrimario,
    c.dhcpHabilitado
FROM
    DispositivoIoT d
INNER JOIN
    ConfiguracionRed c ON d.id = c.dispositivo_id
WHERE
    d.eliminado = FALSE
    AND c.eliminado = FALSE;

-- =====================================================
-- Información de la base de datos
-- =====================================================
SELECT 'Base de datos creada exitosamente' AS mensaje;
SELECT TABLE_NAME, TABLE_ROWS, AUTO_INCREMENT
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'iot' AND TABLE_TYPE = 'BASE TABLE';
