package main;

import config.DatabaseConnection;

/**
 * Clase principal del sistema de gestión de dispositivos IoT.
 * Punto de entrada de la aplicación.
 *
 * @author Mauricio López
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {
        mostrarBanner();

        // Verificar conexión a la base de datos
        if (!DatabaseConnection.testConnection()) {
            System.err.println("\n❌ ERROR: No se pudo conectar a la base de datos.");
            System.err.println("Verifique:");
            System.err.println("  1. El archivo config.properties existe y tiene las credenciales correctas");
            System.err.println("  2. El servidor MySQL está ejecutándose");
            System.err.println("  3. La base de datos 'iot' existe (ejecute sql/schema.sql)");
            System.exit(1);
        }

        System.out.println("✅ Conexión a la base de datos exitosa\n");

        // Iniciar el menú principal
        AppMenu menu = new AppMenu();
        menu.mostrarMenuPrincipal();

        System.out.println("\n¡Gracias por usar el Sistema de Gestión de Dispositivos IoT!");
        System.out.println("Desarrollado por: Gustavo Tiseira, David Vergara, Mauricio López");
    }

    /**
     * Muestra el banner de bienvenida de la aplicación.
     */
    private static void mostrarBanner() {
        System.out.println("\n");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("           SISTEMA DE GESTIÓN DE DISPOSITIVOS IoT             ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("  TPI - Programación 2 - UTN                                  ");
        System.out.println("  Versión 1.0.0 - 2025                                         ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();
    }
}
