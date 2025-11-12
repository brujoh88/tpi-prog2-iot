package service;

import java.util.List;

/**
 * Interface genérica para servicios.
 * Define el contrato de operaciones de negocio que deben cumplir todos los servicios.
 *
 * IMPORTANTE: Los servicios gestionan transacciones internamente.
 *
 * @param <T> tipo de entidad que gestiona el servicio
 * @author David Vergara
 * @version 1.0
 */
public interface GenericService<T> {

    /**
     * Inserta una nueva entidad.
     *
     * @param entity entidad a insertar
     * @throws Exception si ocurre un error en la validación o inserción
     */
    void insertar(T entity) throws Exception;

    /**
     * Actualiza una entidad existente.
     *
     * @param entity entidad con los nuevos datos
     * @throws Exception si ocurre un error en la validación o actualización
     */
    void actualizar(T entity) throws Exception;

    /**
     * Elimina lógicamente una entidad por su ID.
     *
     * @param id identificador de la entidad
     * @throws Exception si ocurre un error en la eliminación
     */
    void eliminar(long id) throws Exception;

    /**
     * Obtiene una entidad por su ID.
     *
     * @param id identificador de la entidad
     * @return la entidad encontrada
     * @throws Exception si la entidad no existe
     */
    T getById(long id) throws Exception;

    /**
     * Obtiene todas las entidades activas.
     *
     * @return lista de entidades activas
     * @throws Exception si ocurre un error en la consulta
     */
    List<T> getAll() throws Exception;
}
