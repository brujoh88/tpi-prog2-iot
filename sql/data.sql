-- =====================================================
-- TPI Programación 2 - Sistema de Gestión de IoT
-- Archivo: data.sql
-- Descripción: Datos de prueba para la aplicación
-- Autor: Mauricio López
-- Fecha: 2024-11
-- =====================================================

USE iot;

-- =====================================================
-- Limpiar datos existentes (si los hay)
-- =====================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE ConfiguracionRed;
TRUNCATE TABLE DispositivoIoT;
SET FOREIGN_KEY_CHECKS = 1;

-- =====================================================
-- Insertar Dispositivos IoT (10 registros de prueba)
-- =====================================================
INSERT INTO DispositivoIoT (serial, modelo, ubicacion, firmwareVersion) VALUES
('SER-A001', 'SENSORTEMP', 'Sala A', 'v1.0.0'),
('SER-A002', 'SENSORHUMEDAD', 'Sala B', 'v1.1.0'),
('SER-A003', 'ACTUADORLUZ', 'Pasillo C', 'v1.0.1'),
('SER-A004', 'GATEWAY', 'Rack Principal', 'v2.0.0'),
('SER-A005', 'CAMARAIP', 'Exterior Norte', 'v3.5.0'),
('SER-A006', 'SENSORPRESION', 'Almacen', 'v1.0.0'),
('SER-A007', 'TERMOSTATO', 'Oficina 1', 'v1.2.0'),
('SER-A008', 'MEDIDORENERGIA', 'Tablero Principal', 'v2.1.0'),
('SER-A009', 'LECTORRFID', 'Entrada Principal', 'v1.5.0'),
('SER-A010', 'CONTROLACCESO', 'Servidor', 'v4.0.0');

-- =====================================================
-- Insertar Configuraciones de Red (10 registros)
-- Asociadas 1:1 con los dispositivos creados
-- =====================================================
INSERT INTO ConfiguracionRed (ip, mascara, gateway, dnsPrimario, dhcpHabilitado, dispositivo_id) VALUES
('192.168.1.10', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 1),
('192.168.1.11', '255.255.255.0', '192.168.1.1', '8.8.4.4', FALSE, 2),
('192.168.1.12', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 3),
('192.168.1.13', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 4),
('192.168.1.14', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 5),
('192.168.1.15', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 6),
('192.168.1.16', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 7),
('192.168.1.17', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 8),
('192.168.1.18', '255.255.255.0', '192.168.1.1', '8.8.8.8', FALSE, 9),
('0.0.0.0', '0.0.0.0', '0.0.0.0', '0.0.0.0', TRUE, 10);

-- =====================================================
-- Verificación de datos insertados
-- =====================================================
SELECT '✓ Datos insertados correctamente' AS mensaje;

SELECT COUNT(*) AS total_dispositivos
FROM DispositivoIoT
WHERE eliminado = FALSE;

SELECT COUNT(*) AS total_configuraciones
FROM ConfiguracionRed
WHERE eliminado = FALSE;

-- Mostrar primeros 5 registros de la vista
SELECT * FROM Vista_Inventario_Red_Activo LIMIT 5;

-- =====================================================
-- Verificar relación 1:1
-- =====================================================
SELECT
    'Verificación de relación 1:1' AS verificacion,
    COUNT(DISTINCT dispositivo_id) AS dispositivos_unicos,
    COUNT(*) AS total_configuraciones
FROM ConfiguracionRed
WHERE eliminado = FALSE;
