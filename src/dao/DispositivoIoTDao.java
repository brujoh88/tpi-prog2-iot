package dao;

import entities.ConfiguracionRed;
import entities.DispositivoIoT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad DispositivoIoT.
 * Implementa operaciones CRUD y búsquedas específicas.
 *
 * IMPORTANTE: Usa PreparedStatement para prevenir SQL Injection.
 * NO crea ni cierra conexiones (recibe Connection externa para transacciones).
 *
 * @author Gustavo Tiseira
 * @version 1.0
 */
public class DispositivoIoTDao implements GenericDao<DispositivoIoT> {

    private final ConfiguracionRedDao configuracionRedDao;

    public DispositivoIoTDao() {
        this.configuracionRedDao = new ConfiguracionRedDao();
    }

    @Override
    public void crear(DispositivoIoT entity, Connection conn) throws SQLException {
        String sql = "INSERT INTO DispositivoIoT (eliminado, serial, modelo, ubicacion, firmwareVersion) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setBoolean(1, entity.getEliminado() != null ? entity.getEliminado() : false);
            pstmt.setString(2, entity.getSerial());
            pstmt.setString(3, entity.getModelo());
            pstmt.setString(4, entity.getUbicacion());
            pstmt.setString(5, entity.getFirmwareVersion());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al crear DispositivoIoT, ninguna fila afectada.");
            }

            // Recuperar el ID generado
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al crear DispositivoIoT, no se obtuvo el ID.");
                }
            }

            // Si tiene configuración de red, crearla y asociarla
            if (entity.getConfiguracionRed() != null) {
                configuracionRedDao.crear(entity.getConfiguracionRed(), conn);
                // Actualizar el dispositivo con la FK a la configuración
                actualizarConfiguracionRed(entity.getId(), entity.getConfiguracionRed().getId(), conn);
            }
        }
    }

    @Override
    public DispositivoIoT leer(long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM DispositivoIoT WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs, conn);
                }
            }
        }

        return null;
    }

    @Override
    public List<DispositivoIoT> leerTodos(Connection conn) throws SQLException {
        List<DispositivoIoT> dispositivos = new ArrayList<>();
        String sql = "SELECT * FROM DispositivoIoT WHERE eliminado = FALSE ORDER BY id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                dispositivos.add(mapResultSetToEntity(rs, conn));
            }
        }

        return dispositivos;
    }

    @Override
    public void actualizar(DispositivoIoT entity, Connection conn) throws SQLException {
        String sql = "UPDATE DispositivoIoT SET serial = ?, modelo = ?, ubicacion = ?, " +
                     "firmwareVersion = ?, eliminado = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getSerial());
            pstmt.setString(2, entity.getModelo());
            pstmt.setString(3, entity.getUbicacion());
            pstmt.setString(4, entity.getFirmwareVersion());
            pstmt.setBoolean(5, entity.getEliminado());
            pstmt.setLong(6, entity.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar DispositivoIoT, ninguna fila afectada.");
            }
        }
    }

    @Override
    public void eliminar(long id, Connection conn) throws SQLException {
        String sql = "UPDATE DispositivoIoT SET eliminado = TRUE WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al eliminar DispositivoIoT, ninguna fila afectada.");
            }
        }
    }

    /**
     * Busca un dispositivo por su número de serie.
     *
     * @param serial número de serie del dispositivo
     * @param conn conexión a la BD
     * @return el dispositivo encontrado, o null si no existe
     * @throws SQLException si hay un error en la operación
     */
    public DispositivoIoT buscarPorSerial(String serial, Connection conn) throws SQLException {
        String sql = "SELECT * FROM DispositivoIoT WHERE serial = ? AND eliminado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, serial);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs, conn);
                }
            }
        }

        return null;
    }

    /**
     * Busca dispositivos por ubicación.
     *
     * @param ubicacion ubicación a buscar
     * @param conn conexión a la BD
     * @return lista de dispositivos en esa ubicación
     * @throws SQLException si hay un error en la operación
     */
    public List<DispositivoIoT> buscarPorUbicacion(String ubicacion, Connection conn) throws SQLException {
        List<DispositivoIoT> dispositivos = new ArrayList<>();
        String sql = "SELECT * FROM DispositivoIoT WHERE ubicacion LIKE ? AND eliminado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + ubicacion + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    dispositivos.add(mapResultSetToEntity(rs, conn));
                }
            }
        }

        return dispositivos;
    }

    /**
     * Obtiene la configuración de red de un dispositivo.
     *
     * @param dispositivoId ID del dispositivo
     * @param conn conexión a la BD
     * @return la configuración de red, o null si no tiene
     * @throws SQLException si hay un error en la operación
     */
    public ConfiguracionRed obtenerConfiguracionRed(long dispositivoId, Connection conn) throws SQLException {
        String sql = "SELECT id FROM ConfiguracionRed WHERE dispositivo_id = ? AND eliminado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dispositivoId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    long configId = rs.getLong("id");
                    return configuracionRedDao.leer(configId, conn);
                }
            }
        }

        return null;
    }

    /**
     * Actualiza la referencia a la configuración de red en un dispositivo.
     *
     * @param dispositivoId ID del dispositivo
     * @param configuracionId ID de la configuración de red
     * @param conn conexión a la BD
     * @throws SQLException si hay un error en la operación
     */
    private void actualizarConfiguracionRed(long dispositivoId, long configuracionId, Connection conn) throws SQLException {
        String sql = "UPDATE ConfiguracionRed SET dispositivo_id = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, dispositivoId);
            pstmt.setLong(2, configuracionId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al asociar configuración de red al dispositivo.");
            }
        }
    }

    /**
     * Mapea un ResultSet a una entidad DispositivoIoT.
     * Carga también su ConfiguracionRed asociada si existe.
     *
     * @param rs ResultSet con los datos
     * @param conn conexión a la BD
     * @return entidad DispositivoIoT poblada
     * @throws SQLException si hay un error en la operación
     */
    private DispositivoIoT mapResultSetToEntity(ResultSet rs, Connection conn) throws SQLException {
        DispositivoIoT dispositivo = new DispositivoIoT();
        dispositivo.setId(rs.getLong("id"));
        dispositivo.setEliminado(rs.getBoolean("eliminado"));
        dispositivo.setSerial(rs.getString("serial"));
        dispositivo.setModelo(rs.getString("modelo"));
        dispositivo.setUbicacion(rs.getString("ubicacion"));
        dispositivo.setFirmwareVersion(rs.getString("firmwareVersion"));

        // Cargar ConfiguracionRed asociada
        ConfiguracionRed configuracion = obtenerConfiguracionRed(dispositivo.getId(), conn);
        dispositivo.setConfiguracionRed(configuracion);

        return dispositivo;
    }
}
