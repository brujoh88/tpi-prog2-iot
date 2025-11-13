package service;

import config.DatabaseConnection;
import dao.ConfiguracionRedDao;
import dao.DispositivoIoTDao;
import entities.ConfiguracionRed;
import entities.DispositivoIoT;
import exceptions.*;
import util.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestión de DispositivoIoT.
 * Implementa la lógica de negocio, validaciones y transacciones.
 *
 * @author David Vergara
 * @version 1.0
 */
public class DispositivoIoTService implements GenericService<DispositivoIoT> {

    private final DispositivoIoTDao dispositivoDao;
    private final ConfiguracionRedDao configuracionDao;

    public DispositivoIoTService() {
        this.dispositivoDao = new DispositivoIoTDao();
        this.configuracionDao = new ConfiguracionRedDao();
    }

    @Override
    public void insertar(DispositivoIoT entity) throws Exception {
        // Validaciones
        validarDispositivo(entity);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Verificar que el serial no exista
            if (existeSerial(entity.getSerial(), conn)) {
                throw new DuplicateEntityException("Ya existe un dispositivo con el serial: " + entity.getSerial());
            }

            // Crear el dispositivo
            dispositivoDao.crear(entity, conn);

            conn.commit();
            System.out.println("[DispositivoIoTService] Dispositivo creado exitosamente con ID: " + entity.getId());

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("[DispositivoIoTService] Rollback ejecutado debido a error SQL");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw manejarErrorSQL(e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("[DispositivoIoTService] Rollback ejecutado debido a error: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Crea un dispositivo IoT junto con su configuración de red en una transacción atómica.
     * Si falla cualquier operación, se hace rollback de todo.
     *
     * @param dispositivo dispositivo a crear
     * @param configuracion configuración de red a asociar
     * @throws Exception si ocurre un error en la validación o creación
     */
    public void insertarDispositivoConConfiguracion(DispositivoIoT dispositivo, ConfiguracionRed configuracion)
            throws Exception {
        // Validaciones
        validarDispositivo(dispositivo);
        validarConfiguracion(configuracion);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Verificar que el serial no exista
            if (existeSerial(dispositivo.getSerial(), conn)) {
                throw new DuplicateEntityException("Ya existe un dispositivo con el serial: " + dispositivo.getSerial());
            }

            // 2. Verificar que la IP no exista (si no es DHCP)
            if (!configuracion.getDhcpHabilitado() && configuracionDao.existeIp(configuracion.getIp(), conn)) {
                throw new DuplicateEntityException("Ya existe una configuración con la IP: " + configuracion.getIp());
            }

            // 3. Crear el DispositivoIoT PRIMERO para obtener su ID
            dispositivoDao.crear(dispositivo, conn);
            System.out.println("[DispositivoIoTService] DispositivoIoT creado con ID: " + dispositivo.getId());

            // 4. Setear el dispositivo_id en la configuración (FK requerida)
            configuracion.setDispositivoId(dispositivo.getId());

            // 5. Crear la ConfiguracionRed con el dispositivo_id válido
            configuracionDao.crear(configuracion, conn);
            System.out.println("[DispositivoIoTService] ConfiguracionRed creada con ID: " + configuracion.getId());

            // 6. Asociar la configuración al dispositivo en memoria (para retornar completo)
            dispositivo.setConfiguracionRed(configuracion);

            // 7. Commit de la transacción
            conn.commit();
            System.out.println("[DispositivoIoTService] Transacción completada exitosamente");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("[DispositivoIoTService] Rollback ejecutado debido a error SQL");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw manejarErrorSQL(e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("[DispositivoIoTService] Rollback ejecutado debido a error: " + e.getMessage());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void actualizar(DispositivoIoT entity) throws Exception {
        // Validaciones
        validarDispositivo(entity);
        Validator.validarId(entity.getId());

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Verificar que el dispositivo exista
            DispositivoIoT existente = dispositivoDao.leer(entity.getId(), conn);
            if (existente == null) {
                throw new EntityNotFoundException("No se encontró el dispositivo con ID: " + entity.getId());
            }

            // Verificar que el serial no esté duplicado (excepto el actual)
            DispositivoIoT dispositivoConSerial = dispositivoDao.buscarPorSerial(entity.getSerial(), conn);
            if (dispositivoConSerial != null && !dispositivoConSerial.getId().equals(entity.getId())) {
                throw new DuplicateEntityException("Ya existe otro dispositivo con el serial: " + entity.getSerial());
            }

            // Actualizar el dispositivo
            dispositivoDao.actualizar(entity, conn);

            conn.commit();
            System.out.println("[DispositivoIoTService] Dispositivo actualizado exitosamente");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw manejarErrorSQL(e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void eliminar(long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Verificar que el dispositivo exista
            DispositivoIoT dispositivo = dispositivoDao.leer(id, conn);
            if (dispositivo == null) {
                throw new EntityNotFoundException("No se encontró el dispositivo con ID: " + id);
            }

            // Eliminar lógicamente el dispositivo
            dispositivoDao.eliminar(id, conn);

            // También eliminar la configuración de red asociada
            if (dispositivo.getConfiguracionRed() != null) {
                configuracionDao.eliminar(dispositivo.getConfiguracionRed().getId(), conn);
            }

            conn.commit();
            System.out.println("[DispositivoIoTService] Dispositivo eliminado lógicamente");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw manejarErrorSQL(e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public DispositivoIoT getById(long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DispositivoIoT dispositivo = dispositivoDao.leer(id, conn);

            if (dispositivo == null) {
                throw new EntityNotFoundException("No se encontró el dispositivo con ID: " + id);
            }

            return dispositivo;

        } catch (SQLException e) {
            throw new DataAccessException("Error al obtener el dispositivo", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public List<DispositivoIoT> getAll() throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return dispositivoDao.leerTodos(conn);

        } catch (SQLException e) {
            throw new DataAccessException("Error al obtener la lista de dispositivos", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Busca un dispositivo por su serial.
     *
     * @param serial serial del dispositivo
     * @return el dispositivo encontrado
     * @throws Exception si no se encuentra o hay un error
     */
    public DispositivoIoT buscarPorSerial(String serial) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DispositivoIoT dispositivo = dispositivoDao.buscarPorSerial(serial, conn);

            if (dispositivo == null) {
                throw new EntityNotFoundException("No se encontró el dispositivo con serial: " + serial);
            }

            return dispositivo;

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar el dispositivo por serial", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Busca dispositivos por ubicación.
     *
     * @param ubicacion ubicación a buscar
     * @return lista de dispositivos en esa ubicación
     * @throws Exception si hay un error
     */
    public List<DispositivoIoT> buscarPorUbicacion(String ubicacion) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return dispositivoDao.buscarPorUbicacion(ubicacion, conn);

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar dispositivos por ubicación", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Valida un dispositivo IoT.
     *
     * @param dispositivo dispositivo a validar
     * @throws ValidationException si hay errores de validación
     */
    private void validarDispositivo(DispositivoIoT dispositivo) throws ValidationException {
        Validator.validarNoNulo(dispositivo, "Dispositivo");
        Validator.validarNoVacio(dispositivo.getSerial(), "Serial");
        Validator.validarNoVacio(dispositivo.getModelo(), "Modelo");
        Validator.validarNoVacio(dispositivo.getUbicacion(), "Ubicación");

        // Normalizar serial y modelo a mayúsculas
        dispositivo.setSerial(Validator.normalizarString(dispositivo.getSerial()));
        dispositivo.setModelo(Validator.normalizarString(dispositivo.getModelo()));

        // Validar formato del serial
        Validator.validarFormatoSerial(dispositivo.getSerial());

        // Validar firmware si está presente
        if (dispositivo.getFirmwareVersion() != null && !dispositivo.getFirmwareVersion().isEmpty()) {
            Validator.validarFormatoFirmware(dispositivo.getFirmwareVersion());
        }

        // Validar longitudes
        Validator.validarLongitud(dispositivo.getSerial(), "Serial", 1, 50);
        Validator.validarLongitud(dispositivo.getModelo(), "Modelo", 1, 50);
        Validator.validarLongitud(dispositivo.getUbicacion(), "Ubicación", 1, 120);
    }

    /**
     * Valida una configuración de red.
     *
     * @param configuracion configuración a validar
     * @throws ValidationException si hay errores de validación
     */
    private void validarConfiguracion(ConfiguracionRed configuracion) throws ValidationException {
        Validator.validarNoNulo(configuracion, "ConfiguracionRed");
        Validator.validarNoNulo(configuracion.getDhcpHabilitado(), "DHCP Habilitado");

        // Validar coherencia de DHCP
        Validator.validarCoherenciaDhcp(configuracion.getDhcpHabilitado(), configuracion.getIp());

        // Si no es DHCP, validar formatos de IPs
        if (!configuracion.getDhcpHabilitado()) {
            Validator.validarFormatoIp(configuracion.getIp());
            Validator.validarFormatoIp(configuracion.getMascara());
            Validator.validarFormatoIp(configuracion.getGateway());
            Validator.validarFormatoIp(configuracion.getDnsPrimario());
        }
    }

    /**
     * Verifica si ya existe un dispositivo con el serial dado.
     *
     * @param serial serial a verificar
     * @param conn conexión a la BD
     * @return true si existe, false en caso contrario
     */
    private boolean existeSerial(String serial, Connection conn) throws SQLException {
        return dispositivoDao.buscarPorSerial(serial, conn) != null;
    }

    /**
     * Maneja errores SQL y los convierte en excepciones de negocio.
     *
     * @param e excepción SQL
     * @return excepción de negocio correspondiente
     */
    private Exception manejarErrorSQL(SQLException e) {
        int errorCode = e.getErrorCode();
        String mensaje = e.getMessage();

        switch (errorCode) {
            case 1062:  // Duplicate entry
                if (mensaje.contains("serial")) {
                    return new DuplicateEntityException("Serial duplicado", e);
                } else if (mensaje.contains("ip")) {
                    return new DuplicateEntityException("IP duplicada", e);
                }
                return new DuplicateEntityException("Valor duplicado", e);

            case 1452:  // Foreign key constraint fails
                return new EntityNotFoundException("La entidad referenciada no existe", e);

            case 3819:  // Check constraint violated
                return new ValidationException("Violación de restricción de validación", e);

            case 1213:  // Deadlock
                return new ConcurrencyException("Deadlock detectado, reintente la operación", e);

            default:
                return new DataAccessException("Error de base de datos: " + mensaje, e);
        }
    }
}
