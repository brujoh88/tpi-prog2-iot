package util;

import java.util.Scanner;

/**
 * Clase de utilidad para manejar entrada de datos desde la consola.
 * Proporciona métodos robustos para leer diferentes tipos de datos.
 *
 * @author David Vergara
 * @version 1.0
 */
public class InputHelper {

    private final Scanner scanner;

    public InputHelper() {
        this.scanner = new Scanner(System.in);
    }

    public InputHelper(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Lee una línea de texto desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return texto ingresado por el usuario
     */
    public String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    /**
     * Lee una línea de texto no vacía desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return texto ingresado (no vacío)
     */
    public String leerStringNoVacio(String mensaje) {
        String input;
        do {
            System.out.print(mensaje);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("❌ El campo no puede estar vacío. Intente nuevamente.");
            }
        } while (input.isEmpty());
        return input;
    }

    /**
     * Lee un número entero desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return número entero ingresado
     */
    public int leerInt(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Debe ingresar un número entero.");
            }
        }
    }

    /**
     * Lee un número entero dentro de un rango desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @param min valor mínimo permitido
     * @param max valor máximo permitido
     * @return número entero dentro del rango
     */
    public int leerIntRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerInt(mensaje);
            if (valor >= min && valor <= max) {
                return valor;
            }
            System.out.println("❌ El valor debe estar entre " + min + " y " + max);
        }
    }

    /**
     * Lee un número long desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return número long ingresado
     */
    public long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String input = scanner.nextLine().trim();
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Entrada inválida. Debe ingresar un número entero.");
            }
        }
    }

    /**
     * Lee un número long positivo desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return número long positivo ingresado
     */
    public long leerLongPositivo(String mensaje) {
        while (true) {
            long valor = leerLong(mensaje);
            if (valor > 0) {
                return valor;
            }
            System.out.println("❌ El valor debe ser mayor que 0");
        }
    }

    /**
     * Lee un valor booleano desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return true si el usuario ingresa 's', 'S', 'si', 'SI', 'sí', 'SÍ', 'yes', 'YES'; false en caso contrario
     */
    public boolean leerBoolean(String mensaje) {
        System.out.print(mensaje + " (s/n): ");
        String input = scanner.nextLine().trim().toLowerCase();
        return input.equals("s") || input.equals("si") || input.equals("sí") ||
               input.equals("yes") || input.equals("y");
    }

    /**
     * Lee una dirección IP desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return dirección IP ingresada
     */
    public String leerIp(String mensaje) {
        while (true) {
            String ip = leerStringNoVacio(mensaje);
            try {
                Validator.validarFormatoIp(ip);
                return ip;
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    /**
     * Lee una dirección IP o permite dejarla vacía.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return dirección IP ingresada, o "0.0.0.0" si se deja vacía
     */
    public String leerIpOpcional(String mensaje) {
        System.out.print(mensaje);
        String ip = scanner.nextLine().trim();
        if (ip.isEmpty()) {
            return "0.0.0.0";
        }
        try {
            Validator.validarFormatoIp(ip);
            return ip;
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
            return leerIpOpcional(mensaje);
        }
    }

    /**
     * Lee un serial de dispositivo desde la consola.
     *
     * @param mensaje mensaje a mostrar al usuario
     * @return serial ingresado (normalizado a mayúsculas)
     */
    public String leerSerial(String mensaje) {
        while (true) {
            String serial = leerStringNoVacio(mensaje);
            serial = serial.toUpperCase();
            try {
                Validator.validarFormatoSerial(serial);
                return serial;
            } catch (Exception e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }

    /**
     * Pausa la ejecución hasta que el usuario presione Enter.
     */
    public void pausar() {
        System.out.print("\nPresione ENTER para continuar...");
        scanner.nextLine();
    }

    /**
     * Limpia la pantalla de la consola.
     */
    public void limpiarPantalla() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            // Si falla, simplemente imprimir líneas en blanco
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Muestra un separador visual en la consola.
     */
    public void mostrarSeparador() {
        System.out.println("═══════════════════════════════════════════════════════════");
    }

    /**
     * Muestra un encabezado con formato.
     *
     * @param titulo título del encabezado
     */
    public void mostrarEncabezado(String titulo) {
        mostrarSeparador();
        System.out.println("  " + titulo);
        mostrarSeparador();
    }

    /**
     * Muestra un mensaje de éxito.
     *
     * @param mensaje mensaje a mostrar
     */
    public void mostrarExito(String mensaje) {
        System.out.println("✅ " + mensaje);
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje mensaje a mostrar
     */
    public void mostrarError(String mensaje) {
        System.out.println("❌ ERROR: " + mensaje);
    }

    /**
     * Muestra un mensaje de advertencia.
     *
     * @param mensaje mensaje a mostrar
     */
    public void mostrarAdvertencia(String mensaje) {
        System.out.println("⚠️  ADVERTENCIA: " + mensaje);
    }

    /**
     * Cierra el scanner.
     */
    public void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
