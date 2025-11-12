package exceptions;

/**
 * Excepción lanzada cuando se intenta crear una entidad con un valor único duplicado.
 * Por ejemplo: Serial de dispositivo duplicado, IP duplicada, etc.
 *
 * @author David Vergara
 * @version 1.0
 */
public class DuplicateEntityException extends Exception {

    public DuplicateEntityException(String message) {
        super(message);
    }

    public DuplicateEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}
