package main;

import entities.ConfiguracionRed;
import entities.DispositivoIoT;
import service.ConfiguracionRedService;
import service.DispositivoIoTService;
import util.InputHelper;

import java.util.List;

/**
 * Menú interactivo de consola para el sistema de gestión de dispositivos IoT.
 * Proporciona CRUD completo para DispositivoIoT y ConfiguracionRed.
 *
 * @author Mauricio López
 * @version 1.0
 */
public class AppMenu {

    private final InputHelper input;
    private final DispositivoIoTService dispositivoService;
    private final ConfiguracionRedService configuracionService;

    public AppMenu() {
        this.input = new InputHelper();
        this.dispositivoService = new DispositivoIoTService();
        this.configuracionService = new ConfiguracionRedService();
    }

    /**
     * Muestra el menú principal de la aplicación.
     */
    public void mostrarMenuPrincipal() {
        boolean salir = false;

        while (!salir) {
            try {
                input.mostrarSeparador();
                System.out.println("    MENÚ PRINCIPAL");
                input.mostrarSeparador();
                System.out.println("  1. Gestión de Dispositivos IoT");
                System.out.println("  2. Gestión de Configuraciones de Red");
                System.out.println("  3. Operaciones Avanzadas");
                System.out.println("  4. Salir");
                input.mostrarSeparador();

                int opcion = input.leerIntRango("Seleccione una opción: ", 1, 4);

                switch (opcion) {
                    case 1:
                        menuDispositivos();
                        break;
                    case 2:
                        menuConfiguraciones();
                        break;
                    case 3:
                        menuOperacionesAvanzadas();
                        break;
                    case 4:
                        salir = true;
                        break;
                }

            } catch (Exception e) {
                input.mostrarError("Error inesperado: " + e.getMessage());
                e.printStackTrace();
                input.pausar();
            }
        }
    }

    /**
     * Menú de gestión de dispositivos IoT.
     */
    private void menuDispositivos() {
        boolean volver = false;

        while (!volver) {
            try {
                input.mostrarEncabezado("GESTIÓN DE DISPOSITIVOS IoT");
                System.out.println("  1. Crear nuevo dispositivo");
                System.out.println("  2. Ver dispositivo por ID");
                System.out.println("  3. Listar todos los dispositivos");
                System.out.println("  4. Buscar dispositivo por serial");
                System.out.println("  5. Buscar dispositivos por ubicación");
                System.out.println("  6. Actualizar dispositivo");
                System.out.println("  7. Eliminar dispositivo (baja lógica)");
                System.out.println("  8. Volver al menú principal");
                input.mostrarSeparador();

                int opcion = input.leerIntRango("Seleccione una opción: ", 1, 8);

                switch (opcion) {
                    case 1:
                        crearDispositivo();
                        break;
                    case 2:
                        verDispositivoPorId();
                        break;
                    case 3:
                        listarTodosDispositivos();
                        break;
                    case 4:
                        buscarDispositivoPorSerial();
                        break;
                    case 5:
                        buscarDispositivosPorUbicacion();
                        break;
                    case 6:
                        actualizarDispositivo();
                        break;
                    case 7:
                        eliminarDispositivo();
                        break;
                    case 8:
                        volver = true;
                        break;
                }

            } catch (Exception e) {
                input.mostrarError(e.getMessage());
                input.pausar();
            }
        }
    }

    /**
     * Menú de gestión de configuraciones de red.
     */
    private void menuConfiguraciones() {
        boolean volver = false;

        while (!volver) {
            try {
                input.mostrarEncabezado("GESTIÓN DE CONFIGURACIONES DE RED");
                System.out.println("  1. Crear nueva configuración");
                System.out.println("  2. Ver configuración por ID");
                System.out.println("  3. Listar todas las configuraciones");
                System.out.println("  4. Buscar configuración por IP");
                System.out.println("  5. Actualizar configuración");
                System.out.println("  6. Eliminar configuración (baja lógica)");
                System.out.println("  7. Volver al menú principal");
                input.mostrarSeparador();

                int opcion = input.leerIntRango("Seleccione una opción: ", 1, 7);

                switch (opcion) {
                    case 1:
                        crearConfiguracion();
                        break;
                    case 2:
                        verConfiguracionPorId();
                        break;
                    case 3:
                        listarTodasConfiguraciones();
                        break;
                    case 4:
                        buscarConfiguracionPorIp();
                        break;
                    case 5:
                        actualizarConfiguracion();
                        break;
                    case 6:
                        eliminarConfiguracion();
                        break;
                    case 7:
                        volver = true;
                        break;
                }

            } catch (Exception e) {
                input.mostrarError(e.getMessage());
                input.pausar();
            }
        }
    }

    /**
     * Menú de operaciones avanzadas.
     */
    private void menuOperacionesAvanzadas() {
        boolean volver = false;

        while (!volver) {
            try {
                input.mostrarEncabezado("OPERACIONES AVANZADAS");
                System.out.println("  1. Crear dispositivo CON configuración de red (transacción)");
                System.out.println("  2. Ver estadísticas del sistema");
                System.out.println("  3. Volver al menú principal");
                input.mostrarSeparador();

                int opcion = input.leerIntRango("Seleccione una opción: ", 1, 3);

                switch (opcion) {
                    case 1:
                        crearDispositivoConConfiguracion();
                        break;
                    case 2:
                        verEstadisticas();
                        break;
                    case 3:
                        volver = true;
                        break;
                }

            } catch (Exception e) {
                input.mostrarError(e.getMessage());
                input.pausar();
            }
        }
    }

    // =====================================================
    // OPERACIONES CRUD - DISPOSITIVO IoT
    // =====================================================

    private void crearDispositivo() throws Exception {
        input.mostrarEncabezado("CREAR NUEVO DISPOSITIVO IoT");

        String serial = input.leerSerial("Serial (formato XXX-XXXX): ");
        String modelo = input.leerStringNoVacio("Modelo: ").toUpperCase();
        String ubicacion = input.leerStringNoVacio("Ubicación: ");
        String firmware = input.leerString("Versión de Firmware (ej: v1.0.0, Enter para omitir): ");

        DispositivoIoT dispositivo = new DispositivoIoT(serial, modelo, ubicacion,
                                                        firmware.isEmpty() ? null : firmware);

        dispositivoService.insertar(dispositivo);
        input.mostrarExito("Dispositivo creado exitosamente con ID: " + dispositivo.getId());
        input.pausar();
    }

    private void verDispositivoPorId() throws Exception {
        input.mostrarEncabezado("VER DISPOSITIVO POR ID");

        long id = input.leerLongPositivo("Ingrese el ID del dispositivo: ");
        DispositivoIoT dispositivo = dispositivoService.getById(id);

        System.out.println(dispositivo.toDetailedString());
        input.pausar();
    }

    private void listarTodosDispositivos() throws Exception {
        input.mostrarEncabezado("LISTADO DE DISPOSITIVOS ACTIVOS");

        List<DispositivoIoT> dispositivos = dispositivoService.getAll();

        if (dispositivos.isEmpty()) {
            System.out.println("  No hay dispositivos registrados.");
        } else {
            System.out.printf("  %-5s %-12s %-20s %-30s %-15s%n",
                            "ID", "SERIAL", "MODELO", "UBICACIÓN", "FIRMWARE");
            input.mostrarSeparador();

            for (DispositivoIoT d : dispositivos) {
                System.out.printf("  %-5d %-12s %-20s %-30s %-15s%n",
                                d.getId(),
                                d.getSerial(),
                                d.getModelo(),
                                d.getUbicacion(),
                                d.getFirmwareVersion() != null ? d.getFirmwareVersion() : "N/A");
            }

            System.out.println("\n  Total: " + dispositivos.size() + " dispositivo(s)");
        }

        input.pausar();
    }

    private void buscarDispositivoPorSerial() throws Exception {
        input.mostrarEncabezado("BUSCAR DISPOSITIVO POR SERIAL");

        String serial = input.leerSerial("Ingrese el serial: ");
        DispositivoIoT dispositivo = dispositivoService.buscarPorSerial(serial);

        System.out.println(dispositivo.toDetailedString());
        input.pausar();
    }

    private void buscarDispositivosPorUbicacion() throws Exception {
        input.mostrarEncabezado("BUSCAR DISPOSITIVOS POR UBICACIÓN");

        String ubicacion = input.leerStringNoVacio("Ingrese la ubicación (búsqueda parcial): ");
        List<DispositivoIoT> dispositivos = dispositivoService.buscarPorUbicacion(ubicacion);

        if (dispositivos.isEmpty()) {
            System.out.println("  No se encontraron dispositivos en la ubicación: " + ubicacion);
        } else {
            System.out.printf("  %-5s %-12s %-20s %-30s%n", "ID", "SERIAL", "MODELO", "UBICACIÓN");
            input.mostrarSeparador();

            for (DispositivoIoT d : dispositivos) {
                System.out.printf("  %-5d %-12s %-20s %-30s%n",
                                d.getId(), d.getSerial(), d.getModelo(), d.getUbicacion());
            }

            System.out.println("\n  Total: " + dispositivos.size() + " dispositivo(s) encontrado(s)");
        }

        input.pausar();
    }

    private void actualizarDispositivo() throws Exception {
        input.mostrarEncabezado("ACTUALIZAR DISPOSITIVO");

        long id = input.leerLongPositivo("Ingrese el ID del dispositivo a actualizar: ");
        DispositivoIoT dispositivo = dispositivoService.getById(id);

        System.out.println("\n  Datos actuales:");
        System.out.println(dispositivo.toDetailedString());

        System.out.println("\n  Ingrese los nuevos datos (Enter para mantener el valor actual):");

        String serial = input.leerString("Serial [" + dispositivo.getSerial() + "]: ");
        if (!serial.isEmpty()) {
            dispositivo.setSerial(serial.toUpperCase());
        }

        String modelo = input.leerString("Modelo [" + dispositivo.getModelo() + "]: ");
        if (!modelo.isEmpty()) {
            dispositivo.setModelo(modelo.toUpperCase());
        }

        String ubicacion = input.leerString("Ubicación [" + dispositivo.getUbicacion() + "]: ");
        if (!ubicacion.isEmpty()) {
            dispositivo.setUbicacion(ubicacion);
        }

        String firmware = input.leerString("Firmware [" +
                                          (dispositivo.getFirmwareVersion() != null ? dispositivo.getFirmwareVersion() : "N/A") +
                                          "]: ");
        if (!firmware.isEmpty()) {
            dispositivo.setFirmwareVersion(firmware);
        }

        dispositivoService.actualizar(dispositivo);
        input.mostrarExito("Dispositivo actualizado exitosamente");
        input.pausar();
    }

    private void eliminarDispositivo() throws Exception {
        input.mostrarEncabezado("ELIMINAR DISPOSITIVO (BAJA LÓGICA)");

        long id = input.leerLongPositivo("Ingrese el ID del dispositivo a eliminar: ");
        DispositivoIoT dispositivo = dispositivoService.getById(id);

        System.out.println("\n  Dispositivo a eliminar:");
        System.out.println(dispositivo.toDetailedString());

        if (input.leerBoolean("\n¿Está seguro de eliminar este dispositivo?")) {
            dispositivoService.eliminar(id);
            input.mostrarExito("Dispositivo eliminado lógicamente");
        } else {
            System.out.println("  Operación cancelada");
        }

        input.pausar();
    }

    // =====================================================
    // OPERACIONES CRUD - CONFIGURACIÓN DE RED
    // =====================================================

    private void crearConfiguracion() throws Exception {
        input.mostrarEncabezado("CREAR NUEVA CONFIGURACIÓN DE RED");

        boolean dhcp = input.leerBoolean("¿DHCP habilitado?");

        ConfiguracionRed config = new ConfiguracionRed();
        config.setDhcpHabilitado(dhcp);

        if (dhcp) {
            config.setIp("0.0.0.0");
            config.setMascara("0.0.0.0");
            config.setGateway("0.0.0.0");
            config.setDnsPrimario("0.0.0.0");
            System.out.println("  Configuración DHCP (IPs automáticas)");
        } else {
            config.setIp(input.leerIp("IP: "));
            config.setMascara(input.leerIp("Máscara: "));
            config.setGateway(input.leerIp("Gateway: "));
            config.setDnsPrimario(input.leerIp("DNS Primario: "));
        }

        configuracionService.insertar(config);
        input.mostrarExito("Configuración creada exitosamente con ID: " + config.getId());
        input.pausar();
    }

    private void verConfiguracionPorId() throws Exception {
        input.mostrarEncabezado("VER CONFIGURACIÓN POR ID");

        long id = input.leerLongPositivo("Ingrese el ID de la configuración: ");
        ConfiguracionRed config = configuracionService.getById(id);

        System.out.println(config.toDetailedString());
        input.pausar();
    }

    private void listarTodasConfiguraciones() throws Exception {
        input.mostrarEncabezado("LISTADO DE CONFIGURACIONES DE RED ACTIVAS");

        List<ConfiguracionRed> configuraciones = configuracionService.getAll();

        if (configuraciones.isEmpty()) {
            System.out.println("  No hay configuraciones registradas.");
        } else {
            System.out.printf("  %-5s %-16s %-16s %-16s %-16s %-8s%n",
                            "ID", "IP", "MÁSCARA", "GATEWAY", "DNS", "DHCP");
            input.mostrarSeparador();

            for (ConfiguracionRed c : configuraciones) {
                System.out.printf("  %-5d %-16s %-16s %-16s %-16s %-8s%n",
                                c.getId(),
                                c.getIp(),
                                c.getMascara(),
                                c.getGateway(),
                                c.getDnsPrimario(),
                                c.getDhcpHabilitado() ? "Sí" : "No");
            }

            System.out.println("\n  Total: " + configuraciones.size() + " configuración(es)");
        }

        input.pausar();
    }

    private void buscarConfiguracionPorIp() throws Exception {
        input.mostrarEncabezado("BUSCAR CONFIGURACIÓN POR IP");

        String ip = input.leerIp("Ingrese la IP: ");
        ConfiguracionRed config = configuracionService.buscarPorIp(ip);

        System.out.println(config.toDetailedString());
        input.pausar();
    }

    private void actualizarConfiguracion() throws Exception {
        input.mostrarEncabezado("ACTUALIZAR CONFIGURACIÓN DE RED");

        long id = input.leerLongPositivo("Ingrese el ID de la configuración a actualizar: ");
        ConfiguracionRed config = configuracionService.getById(id);

        System.out.println("\n  Datos actuales:");
        System.out.println(config.toDetailedString());

        System.out.println("\n  Ingrese los nuevos datos (Enter para mantener el valor actual):");

        boolean dhcp = input.leerBoolean("¿DHCP habilitado? [" +
                                        (config.getDhcpHabilitado() ? "Sí" : "No") + "]");
        config.setDhcpHabilitado(dhcp);

        if (dhcp) {
            config.setIp("0.0.0.0");
            config.setMascara("0.0.0.0");
            config.setGateway("0.0.0.0");
            config.setDnsPrimario("0.0.0.0");
        } else {
            String ip = input.leerString("IP [" + config.getIp() + "]: ");
            if (!ip.isEmpty()) {
                config.setIp(ip);
            }

            String mascara = input.leerString("Máscara [" + config.getMascara() + "]: ");
            if (!mascara.isEmpty()) {
                config.setMascara(mascara);
            }

            String gateway = input.leerString("Gateway [" + config.getGateway() + "]: ");
            if (!gateway.isEmpty()) {
                config.setGateway(gateway);
            }

            String dns = input.leerString("DNS Primario [" + config.getDnsPrimario() + "]: ");
            if (!dns.isEmpty()) {
                config.setDnsPrimario(dns);
            }
        }

        configuracionService.actualizar(config);
        input.mostrarExito("Configuración actualizada exitosamente");
        input.pausar();
    }

    private void eliminarConfiguracion() throws Exception {
        input.mostrarEncabezado("ELIMINAR CONFIGURACIÓN (BAJA LÓGICA)");

        long id = input.leerLongPositivo("Ingrese el ID de la configuración a eliminar: ");
        ConfiguracionRed config = configuracionService.getById(id);

        System.out.println("\n  Configuración a eliminar:");
        System.out.println(config.toDetailedString());

        if (input.leerBoolean("\n¿Está seguro de eliminar esta configuración?")) {
            configuracionService.eliminar(id);
            input.mostrarExito("Configuración eliminada lógicamente");
        } else {
            System.out.println("  Operación cancelada");
        }

        input.pausar();
    }

    // =====================================================
    // OPERACIONES AVANZADAS
    // =====================================================

    private void crearDispositivoConConfiguracion() throws Exception {
        input.mostrarEncabezado("CREAR DISPOSITIVO + CONFIGURACIÓN (TRANSACCIÓN)");
        System.out.println("  Esta operación creará un dispositivo y su configuración de red");
        System.out.println("  en una TRANSACCIÓN ATÓMICA (commit/rollback).\n");

        // Datos del dispositivo
        System.out.println("  DATOS DEL DISPOSITIVO:");
        String serial = input.leerSerial("  Serial (formato XXX-XXXX): ");
        String modelo = input.leerStringNoVacio("  Modelo: ").toUpperCase();
        String ubicacion = input.leerStringNoVacio("  Ubicación: ");
        String firmware = input.leerString("  Versión de Firmware (Enter para omitir): ");

        DispositivoIoT dispositivo = new DispositivoIoT(serial, modelo, ubicacion,
                                                        firmware.isEmpty() ? null : firmware);

        // Datos de la configuración
        System.out.println("\n  DATOS DE LA CONFIGURACIÓN DE RED:");
        boolean dhcp = input.leerBoolean("  ¿DHCP habilitado?");

        ConfiguracionRed config = new ConfiguracionRed();
        config.setDhcpHabilitado(dhcp);

        if (dhcp) {
            config.setIp("0.0.0.0");
            config.setMascara("0.0.0.0");
            config.setGateway("0.0.0.0");
            config.setDnsPrimario("0.0.0.0");
        } else {
            config.setIp(input.leerIp("  IP: "));
            config.setMascara(input.leerIp("  Máscara: "));
            config.setGateway(input.leerIp("  Gateway: "));
            config.setDnsPrimario(input.leerIp("  DNS Primario: "));
        }

        System.out.println("\n  Iniciando transacción...");
        dispositivoService.insertarDispositivoConConfiguracion(dispositivo, config);

        input.mostrarExito("¡Transacción completada exitosamente!");
        System.out.println("  Dispositivo ID: " + dispositivo.getId());
        System.out.println("  Configuración ID: " + config.getId());

        input.pausar();
    }

    private void verEstadisticas() throws Exception {
        input.mostrarEncabezado("ESTADÍSTICAS DEL SISTEMA");

        List<DispositivoIoT> dispositivos = dispositivoService.getAll();
        List<ConfiguracionRed> configuraciones = configuracionService.getAll();

        System.out.println("  Total de Dispositivos IoT:        " + dispositivos.size());
        System.out.println("  Total de Configuraciones de Red:  " + configuraciones.size());

        long dispositivosConConfig = dispositivos.stream()
                .filter(d -> d.getConfiguracionRed() != null)
                .count();

        System.out.println("  Dispositivos con configuración:   " + dispositivosConConfig);
        System.out.println("  Dispositivos sin configuración:   " + (dispositivos.size() - dispositivosConConfig));

        long configDhcp = configuraciones.stream()
                .filter(ConfiguracionRed::getDhcpHabilitado)
                .count();

        System.out.println("\n  Configuraciones con DHCP:         " + configDhcp);
        System.out.println("  Configuraciones con IP estática:  " + (configuraciones.size() - configDhcp));

        input.pausar();
    }
}
