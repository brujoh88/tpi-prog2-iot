package service;

import config.DatabaseConnection;
import dao.ConfiguracionRedDao;
import entities.ConfiguracionRed;
import exceptions.*;
import util.Validator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para la gestión de ConfiguracionRed.
 * Implementa la lógica de negocio, validaciones y transacciones.
 *
 * @author David Vergara
 * @version 1.0
 */
public class ConfiguracionRedService implements GenericService<ConfiguracionRed> {

    private final ConfiguracionRedDao configuracionDao;

    public ConfiguracionRedService() {
        this.configuracionDao = new ConfiguracionRedDao();
    }

    @Override
    public void insertar(ConfiguracionRed entity) throws Exception {
        // Validaciones
        validarConfiguracion(entity);

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Verificar que la IP no exista (si no es DHCP)
            if (!entity.getDhcpHabilitado() && configuracionDao.existeIp(entity.getIp(), conn)) {
                throw new DuplicateEntityException("Ya existe una configuración con la IP: " + entity.getIp());
            }

            // Crear la configuración
            configuracionDao.crear(entity, conn);

            conn.commit();
            System.out.println("[ConfiguracionRedService] Configuración creada exitosamente con ID: " + entity.getId());

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("[ConfiguracionRedService] Rollback ejecutado debido a error SQL");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw manejarErrorSQL(e);
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("[ConfiguracionRedService] Rollback ejecutado debido a error: " + e.getMessage());
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
    public void actualizar(ConfiguracionRed entity) throws Exception {
        // Validaciones
        validarConfiguracion(entity);
        Validator.validarId(entity.getId());

        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Verificar que la configuración exista
            ConfiguracionRed existente = configuracionDao.leer(entity.getId(), conn);
            if (existente == null) {
                throw new EntityNotFoundException("No se encontró la configuración con ID: " + entity.getId());
            }

            // Verificar que la IP no esté duplicada (excepto la actual)
            if (!entity.getDhcpHabilitado()) {
                ConfiguracionRed configuracionConIp = configuracionDao.buscarPorIp(entity.getIp(), conn);
                if (configuracionConIp != null && !configuracionConIp.getId().equals(entity.getId())) {
                    throw new DuplicateEntityException("Ya existe otra configuración con la IP: " + entity.getIp());
                }
            }

            // Actualizar la configuración
            configuracionDao.actualizar(entity, conn);

            conn.commit();
            System.out.println("[ConfiguracionRedService] Configuración actualizada exitosamente");

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

            // Verificar que la configuración exista
            ConfiguracionRed configuracion = configuracionDao.leer(id, conn);
            if (configuracion == null) {
                throw new EntityNotFoundException("No se encontró la configuración con ID: " + id);
            }

            // Verificar si está asociada a un dispositivo
            if (configuracionDao.estaAsociada(id, conn)) {
                throw new ValidationException("No se puede eliminar la configuración porque está asociada a un dispositivo");
            }

            // Eliminar lógicamente la configuración
            configuracionDao.eliminar(id, conn);

            conn.commit();
            System.out.println("[ConfiguracionRedService] Configuración eliminada lógicamente");

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
    public ConfiguracionRed getById(long id) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            ConfiguracionRed configuracion = configuracionDao.leer(id, conn);

            if (configuracion == null) {
                throw new EntityNotFoundException("No se encontró la configuración con ID: " + id);
            }

            return configuracion;

        } catch (SQLException e) {
            throw new DataAccessException("Error al obtener la configuración", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    @Override
    public List<ConfiguracionRed> getAll() throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return configuracionDao.leerTodos(conn);

        } catch (SQLException e) {
            throw new DataAccessException("Error al obtener la lista de configuraciones", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Busca una configuración por su IP.
     *
     * @param ip dirección IP a buscar
     * @return la configuración encontrada
     * @throws Exception si no se encuentra o hay un error
     */
    public ConfiguracionRed buscarPorIp(String ip) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            ConfiguracionRed configuracion = configuracionDao.buscarPorIp(ip, conn);

            if (configuracion == null) {
                throw new EntityNotFoundException("No se encontró la configuración con IP: " + ip);
            }

            return configuracion;

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar la configuración por IP", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
    }

    /**
     * Busca configuraciones por estado de DHCP.
     *
     * @param dhcpHabilitado true para buscar con DHCP, false para buscar sin DHCP
     * @return lista de configuraciones que cumplen el criterio
     * @throws Exception si hay un error
     */
    public List<ConfiguracionRed> buscarPorDhcp(boolean dhcpHabilitado) throws Exception {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            return configuracionDao.buscarPorDhcp(dhcpHabilitado, conn);

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar configuraciones por DHCP", e);
        } finally {
            DatabaseConnection.closeConnection(conn);
        }
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

            // Máscara, gateway y DNS pueden ser opcionales, pero si están deben ser válidos
            if (configuracion.getMascara() != null && !configuracion.getMascara().isEmpty() &&
                !configuracion.getMascara().equals("0.0.0.0")) {
                Validator.validarFormatoIp(configuracion.getMascara());
            }

            if (configuracion.getGateway() != null && !configuracion.getGateway().isEmpty() &&
                !configuracion.getGateway().equals("0.0.0.0")) {
                Validator.validarFormatoIp(configuracion.getGateway());
            }

            if (configuracion.getDnsPrimario() != null && !configuracion.getDnsPrimario().isEmpty() &&
                !configuracion.getDnsPrimario().equals("0.0.0.0")) {
                Validator.validarFormatoIp(configuracion.getDnsPrimario());
            }
        }
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
                if (mensaje.contains("ip")) {
                    return new DuplicateEntityException("IP duplicada", e);
                } else if (mensaje.contains("dispositivo_id")) {
                    return new ValidationException("Esta configuración ya está asociada a un dispositivo", e);
                }
                return new DuplicateEntityException("Valor duplicado", e);

            case 1452:  // Foreign key constraint fails
                return new EntityNotFoundException("El dispositivo referenciado no existe", e);

            case 3819:  // Check constraint violated
                return new ValidationException("Formato de IP inválido", e);

            case 1213:  // Deadlock
                return new ConcurrencyException("Deadlock detectado, reintente la operación", e);

            default:
                return new DataAccessException("Error de base de datos: " + mensaje, e);
        }
    }
}
