package dao;

import entities.ConfiguracionRed;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object para la entidad ConfiguracionRed.
 * Implementa operaciones CRUD y búsquedas específicas.
 *
 * IMPORTANTE: Usa PreparedStatement para prevenir SQL Injection.
 * NO crea ni cierra conexiones (recibe Connection externa para transacciones).
 *
 * @author Gustavo Tiseira
 * @version 1.0
 */
public class ConfiguracionRedDao implements GenericDao<ConfiguracionRed> {

    @Override
    public void crear(ConfiguracionRed entity, Connection conn) throws SQLException {
        String sql = "INSERT INTO ConfiguracionRed (eliminado, ip, mascara, gateway, dnsPrimario, dhcpHabilitado) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setBoolean(1, entity.getEliminado() != null ? entity.getEliminado() : false);
            pstmt.setString(2, entity.getIp());
            pstmt.setString(3, entity.getMascara());
            pstmt.setString(4, entity.getGateway());
            pstmt.setString(5, entity.getDnsPrimario());
            pstmt.setBoolean(6, entity.getDhcpHabilitado());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al crear ConfiguracionRed, ninguna fila afectada.");
            }

            // Recuperar el ID generado
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                } else {
                    throw new SQLException("Error al crear ConfiguracionRed, no se obtuvo el ID.");
                }
            }
        }
    }

    @Override
    public ConfiguracionRed leer(long id, Connection conn) throws SQLException {
        String sql = "SELECT * FROM ConfiguracionRed WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<ConfiguracionRed> leerTodos(Connection conn) throws SQLException {
        List<ConfiguracionRed> configuraciones = new ArrayList<>();
        String sql = "SELECT * FROM ConfiguracionRed WHERE eliminado = FALSE ORDER BY id";

        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                configuraciones.add(mapResultSetToEntity(rs));
            }
        }

        return configuraciones;
    }

    @Override
    public void actualizar(ConfiguracionRed entity, Connection conn) throws SQLException {
        String sql = "UPDATE ConfiguracionRed SET ip = ?, mascara = ?, gateway = ?, " +
                     "dnsPrimario = ?, dhcpHabilitado = ?, eliminado = ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entity.getIp());
            pstmt.setString(2, entity.getMascara());
            pstmt.setString(3, entity.getGateway());
            pstmt.setString(4, entity.getDnsPrimario());
            pstmt.setBoolean(5, entity.getDhcpHabilitado());
            pstmt.setBoolean(6, entity.getEliminado());
            pstmt.setLong(7, entity.getId());

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al actualizar ConfiguracionRed, ninguna fila afectada.");
            }
        }
    }

    @Override
    public void eliminar(long id, Connection conn) throws SQLException {
        String sql = "UPDATE ConfiguracionRed SET eliminado = TRUE WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Error al eliminar ConfiguracionRed, ninguna fila afectada.");
            }
        }
    }

    /**
     * Busca una configuración de red por su dirección IP.
     *
     * @param ip dirección IP a buscar
     * @param conn conexión a la BD
     * @return la configuración encontrada, o null si no existe
     * @throws SQLException si hay un error en la operación
     */
    public ConfiguracionRed buscarPorIp(String ip, Connection conn) throws SQLException {
        String sql = "SELECT * FROM ConfiguracionRed WHERE ip = ? AND eliminado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEntity(rs);
                }
            }
        }

        return null;
    }

    /**
     * Busca configuraciones de red con DHCP habilitado o deshabilitado.
     *
     * @param dhcpHabilitado true para buscar con DHCP, false para buscar sin DHCP
     * @param conn conexión a la BD
     * @return lista de configuraciones que cumplen el criterio
     * @throws SQLException si hay un error en la operación
     */
    public List<ConfiguracionRed> buscarPorDhcp(boolean dhcpHabilitado, Connection conn) throws SQLException {
        List<ConfiguracionRed> configuraciones = new ArrayList<>();
        String sql = "SELECT * FROM ConfiguracionRed WHERE dhcpHabilitado = ? AND eliminado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, dhcpHabilitado);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    configuraciones.add(mapResultSetToEntity(rs));
                }
            }
        }

        return configuraciones;
    }

    /**
     * Verifica si una IP ya está en uso.
     *
     * @param ip dirección IP a verificar
     * @param conn conexión a la BD
     * @return true si la IP ya existe, false en caso contrario
     * @throws SQLException si hay un error en la operación
     */
    public boolean existeIp(String ip, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ConfiguracionRed WHERE ip = ? AND eliminado = FALSE";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ip);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Verifica si una configuración de red ya está asociada a un dispositivo.
     *
     * @param configuracionId ID de la configuración de red
     * @param conn conexión a la BD
     * @return true si ya está asociada, false en caso contrario
     * @throws SQLException si hay un error en la operación
     */
    public boolean estaAsociada(long configuracionId, Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ConfiguracionRed WHERE id = ? AND dispositivo_id IS NOT NULL";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, configuracionId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Mapea un ResultSet a una entidad ConfiguracionRed.
     *
     * @param rs ResultSet con los datos
     * @return entidad ConfiguracionRed poblada
     * @throws SQLException si hay un error en la operación
     */
    private ConfiguracionRed mapResultSetToEntity(ResultSet rs) throws SQLException {
        ConfiguracionRed configuracion = new ConfiguracionRed();
        configuracion.setId(rs.getLong("id"));
        configuracion.setEliminado(rs.getBoolean("eliminado"));
        configuracion.setIp(rs.getString("ip"));
        configuracion.setMascara(rs.getString("mascara"));
        configuracion.setGateway(rs.getString("gateway"));
        configuracion.setDnsPrimario(rs.getString("dnsPrimario"));
        configuracion.setDhcpHabilitado(rs.getBoolean("dhcpHabilitado"));

        return configuracion;
    }
}
