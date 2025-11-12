package util;

import exceptions.ValidationException;

import java.util.regex.Pattern;

/**
 * Clase de utilidad para validaciones de datos.
 * Contiene métodos estáticos para validar formatos, campos obligatorios, etc.
 *
 * @author David Vergara
 * @version 1.0
 */
public class Validator {

    // Patrones de validación
    private static final Pattern PATRON_IPV4 = Pattern.compile(
        "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$"
    );

    private static final Pattern PATRON_SERIAL = Pattern.compile(
        "^[A-Z]{3}-[A-Z0-9]{4}$"
    );

    private static final Pattern PATRON_FIRMWARE = Pattern.compile(
        "^v\\d+\\.\\d+\\.\\d+$"
    );

    /**
     * Constructor privado para prevenir instanciación.
     */
    private Validator() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no debe ser instanciada");
    }

    /**
     * Valida que un string no sea nulo ni vacío.
     *
     * @param valor string a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws ValidationException si el valor es nulo o vacío
     */
    public static void validarNoVacio(String valor, String nombreCampo) throws ValidationException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new ValidationException("El campo '" + nombreCampo + "' no puede estar vacío");
        }
    }

    /**
     * Valida que un objeto no sea nulo.
     *
     * @param objeto objeto a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws ValidationException si el objeto es nulo
     */
    public static void validarNoNulo(Object objeto, String nombreCampo) throws ValidationException {
        if (objeto == null) {
            throw new ValidationException("El campo '" + nombreCampo + "' no puede ser nulo");
        }
    }

    /**
     * Valida el formato de una dirección IPv4.
     *
     * @param ip dirección IP a validar
     * @throws ValidationException si el formato es inválido
     */
    public static void validarFormatoIp(String ip) throws ValidationException {
        if (ip == null || ip.trim().isEmpty()) {
            throw new ValidationException("La dirección IP no puede estar vacía");
        }

        if (!PATRON_IPV4.matcher(ip).matches()) {
            throw new ValidationException("Formato de dirección IPv4 inválido: " + ip);
        }
    }

    /**
     * Valida el formato de un serial de dispositivo.
     * Formato esperado: XXX-XXXX (ej: SER-A001)
     *
     * @param serial serial a validar
     * @throws ValidationException si el formato es inválido
     */
    public static void validarFormatoSerial(String serial) throws ValidationException {
        if (serial == null || serial.trim().isEmpty()) {
            throw new ValidationException("El serial no puede estar vacío");
        }

        if (!PATRON_SERIAL.matcher(serial).matches()) {
            throw new ValidationException(
                "Formato de serial inválido: " + serial + ". " +
                "Formato esperado: XXX-XXXX (ej: SER-A001)"
            );
        }
    }

    /**
     * Valida el formato de una versión de firmware.
     * Formato esperado: vX.Y.Z (ej: v1.2.3)
     *
     * @param firmware versión de firmware a validar
     * @throws ValidationException si el formato es inválido
     */
    public static void validarFormatoFirmware(String firmware) throws ValidationException {
        if (firmware != null && !firmware.isEmpty()) {
            if (!PATRON_FIRMWARE.matcher(firmware).matches()) {
                throw new ValidationException(
                    "Formato de versión de firmware inválido: " + firmware + ". " +
                    "Formato esperado: vX.Y.Z (ej: v1.2.3)"
                );
            }
        }
    }

    /**
     * Valida que la longitud de un string esté dentro de un rango.
     *
     * @param valor string a validar
     * @param nombreCampo nombre del campo
     * @param longitudMinima longitud mínima permitida
     * @param longitudMaxima longitud máxima permitida
     * @throws ValidationException si la longitud está fuera del rango
     */
    public static void validarLongitud(String valor, String nombreCampo, int longitudMinima, int longitudMaxima)
            throws ValidationException {
        if (valor == null) {
            throw new ValidationException("El campo '" + nombreCampo + "' no puede ser nulo");
        }

        int longitud = valor.length();
        if (longitud < longitudMinima || longitud > longitudMaxima) {
            throw new ValidationException(
                "El campo '" + nombreCampo + "' debe tener entre " + longitudMinima +
                " y " + longitudMaxima + " caracteres (actual: " + longitud + ")"
            );
        }
    }

    /**
     * Valida la coherencia de una configuración de red con DHCP.
     *
     * @param dhcpHabilitado indica si DHCP está habilitado
     * @param ip dirección IP
     * @throws ValidationException si hay incoherencia
     */
    public static void validarCoherenciaDhcp(boolean dhcpHabilitado, String ip) throws ValidationException {
        if (!dhcpHabilitado) {
            // Si DHCP está deshabilitado, debe haber una IP válida
            if (ip == null || ip.isEmpty() || ip.equals("0.0.0.0")) {
                throw new ValidationException(
                    "Si DHCP está deshabilitado, debe proporcionar una dirección IP válida"
                );
            }
        }
    }

    /**
     * Convierte un string a mayúsculas de forma segura.
     *
     * @param valor string a convertir
     * @return string en mayúsculas, o null si el valor es null
     */
    public static String convertirAMayusculas(String valor) {
        return (valor != null) ? valor.toUpperCase() : null;
    }

    /**
     * Limpia y normaliza un string (trim + mayúsculas).
     *
     * @param valor string a normalizar
     * @return string normalizado
     */
    public static String normalizarString(String valor) {
        if (valor == null) {
            return null;
        }
        return valor.trim().toUpperCase();
    }

    /**
     * Valida un número entero positivo.
     *
     * @param valor valor a validar
     * @param nombreCampo nombre del campo
     * @throws ValidationException si el valor no es positivo
     */
    public static void validarPositivo(long valor, String nombreCampo) throws ValidationException {
        if (valor <= 0) {
            throw new ValidationException("El campo '" + nombreCampo + "' debe ser un número positivo");
        }
    }

    /**
     * Valida que un ID sea válido (mayor que 0).
     *
     * @param id ID a validar
     * @throws ValidationException si el ID no es válido
     */
    public static void validarId(Long id) throws ValidationException {
        if (id == null || id <= 0) {
            throw new ValidationException("ID inválido: " + id);
        }
    }
}
