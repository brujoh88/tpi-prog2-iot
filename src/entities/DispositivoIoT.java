package entities;

/**
 * Representa un Dispositivo IoT (Internet of Things) en el sistema.
 * Clase A en la relación unidireccional 1→1 con ConfiguracionRed.
 *
 * @author Gustavo Tiseira
 * @version 1.0
 */
public class DispositivoIoT {

    private Long id;
    private Boolean eliminado;
    private String serial;
    private String modelo;
    private String ubicacion;
    private String firmwareVersion;
    private ConfiguracionRed configuracionRed;  // Relación 1→1 unidireccional

    /**
     * Constructor vacío requerido por el patrón DAO.
     */
    public DispositivoIoT() {
        this.eliminado = false;
    }

    /**
     * Constructor completo con todos los campos.
     *
     * @param id ID del dispositivo
     * @param eliminado indica si está dado de baja lógicamente
     * @param serial número de serie único
     * @param modelo modelo del dispositivo
     * @param ubicacion ubicación física del dispositivo
     * @param firmwareVersion versión del firmware instalado
     * @param configuracionRed configuración de red asociada
     */
    public DispositivoIoT(Long id, Boolean eliminado, String serial, String modelo,
                          String ubicacion, String firmwareVersion, ConfiguracionRed configuracionRed) {
        this.id = id;
        this.eliminado = eliminado;
        this.serial = serial;
        this.modelo = modelo;
        this.ubicacion = ubicacion;
        this.firmwareVersion = firmwareVersion;
        this.configuracionRed = configuracionRed;
    }

    /**
     * Constructor sin ID (para nuevos dispositivos).
     */
    public DispositivoIoT(String serial, String modelo, String ubicacion, String firmwareVersion) {
        this.eliminado = false;
        this.serial = serial;
        this.modelo = modelo;
        this.ubicacion = ubicacion;
        this.firmwareVersion = firmwareVersion;
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getEliminado() {
        return eliminado;
    }

    public void setEliminado(Boolean eliminado) {
        this.eliminado = eliminado;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public ConfiguracionRed getConfiguracionRed() {
        return configuracionRed;
    }

    public void setConfiguracionRed(ConfiguracionRed configuracionRed) {
        this.configuracionRed = configuracionRed;
    }

    /**
     * toString sin recursión - no llama a configuracionRed.toString() para evitar StackOverflow.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DispositivoIoT{");
        sb.append("id=").append(id);
        sb.append(", eliminado=").append(eliminado);
        sb.append(", serial='").append(serial).append('\'');
        sb.append(", modelo='").append(modelo).append('\'');
        sb.append(", ubicacion='").append(ubicacion).append('\'');
        sb.append(", firmwareVersion='").append(firmwareVersion).append('\'');
        sb.append(", tieneConfiguracionRed=").append(configuracionRed != null);
        if (configuracionRed != null) {
            sb.append(", configuracionRedId=").append(configuracionRed.getId());
        }
        sb.append('}');
        return sb.toString();
    }

    /**
     * Representación detallada para mostrar en la consola.
     */
    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n═══════════════════════════════════════════════════════════\n");
        sb.append("  DISPOSITIVO IoT\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append(String.format("  ID:                 %d\n", id));
        sb.append(String.format("  Serial:             %s\n", serial));
        sb.append(String.format("  Modelo:             %s\n", modelo));
        sb.append(String.format("  Ubicación:          %s\n", ubicacion));
        sb.append(String.format("  Firmware:           %s\n", firmwareVersion != null ? firmwareVersion : "N/A"));
        sb.append(String.format("  Estado:             %s\n", eliminado ? "ELIMINADO" : "ACTIVO"));

        if (configuracionRed != null) {
            sb.append("\n  CONFIGURACIÓN DE RED:\n");
            sb.append("  ───────────────────────────────────────────────────────────\n");
            sb.append(String.format("  IP:                 %s\n", configuracionRed.getIp()));
            sb.append(String.format("  Máscara:            %s\n", configuracionRed.getMascara()));
            sb.append(String.format("  Gateway:            %s\n", configuracionRed.getGateway()));
            sb.append(String.format("  DNS Primario:       %s\n", configuracionRed.getDnsPrimario()));
            sb.append(String.format("  DHCP Habilitado:    %s\n", configuracionRed.getDhcpHabilitado() ? "Sí" : "No"));
        } else {
            sb.append("\n  Sin configuración de red asociada\n");
        }

        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }
}
