package dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface genérica para operaciones CRUD (Create, Read, Update, Delete).
 * Define el contrato que deben cumplir todos los DAOs del sistema.
 *
 * IMPORTANTE: Los métodos reciben Connection externa para soportar transacciones.
 *
 * @param <T> tipo de entidad que gestiona el DAO
 * @author Gustavo Tiseira
 * @version 1.0
 */
public interface GenericDao<T> {

    /**
     * Crea una nueva entidad en la base de datos.
     *
     * @param entity entidad a crear
     * @param conn conexión a la BD (externa, para transacciones)
     * @throws SQLException si hay un error en la operación
     */
    void crear(T entity, Connection conn) throws SQLException;

    /**
     * Lee una entidad por su ID.
     *
     * @param id identificador de la entidad
     * @param conn conexión a la BD (externa, para transacciones)
     * @return la entidad encontrada, o null si no existe
     * @throws SQLException si hay un error en la operación
     */
    T leer(long id, Connection conn) throws SQLException;

    /**
     * Lee todas las entidades activas (eliminado = false).
     *
     * @param conn conexión a la BD (externa, para transacciones)
     * @return lista de entidades activas
     * @throws SQLException si hay un error en la operación
     */
    List<T> leerTodos(Connection conn) throws SQLException;

    /**
     * Actualiza una entidad existente.
     *
     * @param entity entidad con los nuevos datos
     * @param conn conexión a la BD (externa, para transacciones)
     * @throws SQLException si hay un error en la operación
     */
    void actualizar(T entity, Connection conn) throws SQLException;

    /**
     * Realiza una baja lógica de una entidad (eliminado = true).
     *
     * @param id identificador de la entidad a eliminar
     * @param conn conexión a la BD (externa, para transacciones)
     * @throws SQLException si hay un error en la operación
     */
    void eliminar(long id, Connection conn) throws SQLException;
}
