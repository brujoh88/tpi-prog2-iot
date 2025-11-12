package exceptions;

/**
 * Excepción lanzada cuando no se encuentra una entidad solicitada.
 *
 * @author David Vergara
 * @version 1.0
 */
public class EntityNotFoundException extends Exception {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
