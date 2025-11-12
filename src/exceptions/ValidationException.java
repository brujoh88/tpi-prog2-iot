package exceptions;

/**
 * Excepción lanzada cuando falla una validación de datos en la capa de negocio.
 * Por ejemplo: formato de IP inválido, serial vacío, etc.
 *
 * @author David Vergara
 * @version 1.0
 */
public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
