package entities;

/**
 * Representa la configuración de red de un dispositivo IoT.
 * Clase B en la relación unidireccional 1→1 con DispositivoIoT.
 *
 * @author Gustavo Tiseira
 * @version 1.0
 */
public class ConfiguracionRed {

    private Long id;
    private Boolean eliminado;
    private String ip;
    private String mascara;
    private String gateway;
    private String dnsPrimario;
    private Boolean dhcpHabilitado;

    /**
     * Constructor vacío requerido por el patrón DAO.
     */
    public ConfiguracionRed() {
        this.eliminado = false;
        this.dhcpHabilitado = false;
    }

    /**
     * Constructor completo con todos los campos.
     *
     * @param id ID de la configuración
     * @param eliminado indica si está dada de baja lógicamente
     * @param ip dirección IP (IPv4 o IPv6)
     * @param mascara máscara de subred
     * @param gateway puerta de enlace
     * @param dnsPrimario servidor DNS primario
     * @param dhcpHabilitado indica si DHCP está habilitado
     */
    public ConfiguracionRed(Long id, Boolean eliminado, String ip, String mascara,
                            String gateway, String dnsPrimario, Boolean dhcpHabilitado) {
        this.id = id;
        this.eliminado = eliminado;
        this.ip = ip;
        this.mascara = mascara;
        this.gateway = gateway;
        this.dnsPrimario = dnsPrimario;
        this.dhcpHabilitado = dhcpHabilitado;
    }

    /**
     * Constructor sin ID (para nuevas configuraciones).
     */
    public ConfiguracionRed(String ip, String mascara, String gateway,
                           String dnsPrimario, Boolean dhcpHabilitado) {
        this.eliminado = false;
        this.ip = ip;
        this.mascara = mascara;
        this.gateway = gateway;
        this.dnsPrimario = dnsPrimario;
        this.dhcpHabilitado = dhcpHabilitado;
    }

    /**
     * Constructor para configuración con DHCP.
     */
    public ConfiguracionRed(Boolean dhcpHabilitado) {
        this.eliminado = false;
        this.dhcpHabilitado = dhcpHabilitado;
        if (dhcpHabilitado) {
            this.ip = "0.0.0.0";
            this.mascara = "0.0.0.0";
            this.gateway = "0.0.0.0";
            this.dnsPrimario = "0.0.0.0";
        }
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMascara() {
        return mascara;
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDnsPrimario() {
        return dnsPrimario;
    }

    public void setDnsPrimario(String dnsPrimario) {
        this.dnsPrimario = dnsPrimario;
    }

    public Boolean getDhcpHabilitado() {
        return dhcpHabilitado;
    }

    public void setDhcpHabilitado(Boolean dhcpHabilitado) {
        this.dhcpHabilitado = dhcpHabilitado;
    }

    @Override
    public String toString() {
        return "ConfiguracionRed{" +
                "id=" + id +
                ", eliminado=" + eliminado +
                ", ip='" + ip + '\'' +
                ", mascara='" + mascara + '\'' +
                ", gateway='" + gateway + '\'' +
                ", dnsPrimario='" + dnsPrimario + '\'' +
                ", dhcpHabilitado=" + dhcpHabilitado +
                '}';
    }

    /**
     * Representación detallada para mostrar en la consola.
     */
    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n═══════════════════════════════════════════════════════════\n");
        sb.append("  CONFIGURACIÓN DE RED\n");
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append(String.format("  ID:                 %d\n", id));
        sb.append(String.format("  IP:                 %s\n", ip != null ? ip : "N/A"));
        sb.append(String.format("  Máscara:            %s\n", mascara != null ? mascara : "N/A"));
        sb.append(String.format("  Gateway:            %s\n", gateway != null ? gateway : "N/A"));
        sb.append(String.format("  DNS Primario:       %s\n", dnsPrimario != null ? dnsPrimario : "N/A"));
        sb.append(String.format("  DHCP Habilitado:    %s\n", dhcpHabilitado ? "Sí" : "No"));
        sb.append(String.format("  Estado:             %s\n", eliminado ? "ELIMINADO" : "ACTIVO"));
        sb.append("═══════════════════════════════════════════════════════════\n");
        return sb.toString();
    }
}
