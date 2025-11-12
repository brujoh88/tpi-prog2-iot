package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase Singleton para gestionar la conexión a la base de datos MySQL.
 * Lee la configuración desde el archivo config.properties.
 *
 * @author Gustavo Tiseira
 * @version 1.0
 */
public class DatabaseConnection {

    private static final String CONFIG_FILE = "config.properties";
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    private static String DRIVER;

    // Bloque estático para cargar la configuración al iniciar la clase
    static {
        loadConfiguration();
    }

    /**
     * Constructor privado para prevenir instanciación (patrón Singleton).
     */
    private DatabaseConnection() {
        throw new UnsupportedOperationException("Esta es una clase de utilidad y no debe ser instanciada");
    }

    /**
     * Carga la configuración desde el archivo config.properties.
     */
    private static void loadConfiguration() {
        Properties props = new Properties();

        try (InputStream input = new FileInputStream(CONFIG_FILE)) {
            props.load(input);

            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
            DRIVER = props.getProperty("db.driver");

            // Cargar el driver JDBC
            Class.forName(DRIVER);

            System.out.println("[DatabaseConnection] Configuración cargada exitosamente");

        } catch (IOException e) {
            System.err.println("[DatabaseConnection] Error al cargar config.properties: " + e.getMessage());
            throw new RuntimeException("No se pudo cargar la configuración de la base de datos", e);
        } catch (ClassNotFoundException e) {
            System.err.println("[DatabaseConnection] Driver JDBC no encontrado: " + e.getMessage());
            throw new RuntimeException("Driver JDBC no encontrado", e);
        }
    }

    /**
     * Obtiene una nueva conexión a la base de datos.
     *
     * @return Connection objeto de conexión a la BD
     * @throws SQLException si hay un error al conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            return conn;
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Error al obtener conexión: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una conexión con credenciales personalizadas.
     * Útil para conexiones con usuarios de solo lectura.
     *
     * @param user usuario de la BD
     * @param password contraseña del usuario
     * @return Connection objeto de conexión a la BD
     * @throws SQLException si hay un error al conectar
     */
    public static Connection getConnection(String user, String password) throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, user, password);
            return conn;
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Error al obtener conexión con credenciales personalizadas: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica si la conexión a la base de datos está disponible.
     *
     * @return true si la conexión es exitosa, false en caso contrario
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("[DatabaseConnection] Test de conexión fallido: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cierra una conexión de forma segura.
     *
     * @param conn conexión a cerrar
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("[DatabaseConnection] Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
