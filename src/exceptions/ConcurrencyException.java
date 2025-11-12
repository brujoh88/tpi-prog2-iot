package exceptions;

/**
 * Excepción lanzada cuando ocurre un problema de concurrencia en la base de datos.
 * Por ejemplo: deadlock detectado.
 *
 * @author David Vergara
 * @version 1.0
 */
public class ConcurrencyException extends Exception {

    public ConcurrencyException(String message) {
        super(message);
    }

    public ConcurrencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
