package exceptions;

/**
 * Excepción lanzada cuando ocurre un error al acceder a la base de datos.
 *
 * @author David Vergara
 * @version 1.0
 */
public class DataAccessException extends Exception {

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
