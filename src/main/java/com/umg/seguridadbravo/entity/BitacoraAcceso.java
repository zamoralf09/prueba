package com.umg.seguridadbravo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "BITACORA_ACCESO")
public class BitacoraAcceso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id_Bitacora_Acceso")
    private Integer idBitacoraAcceso;

    @NotBlank
    @Column(name = "IdUsuario", length = 100, nullable = false)
    private String idUsuario;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdTipoAcceso", nullable = false)
    private TipoAcceso tipoAcceso;

    @NotNull
    @Column(name = "FechaAcceso", nullable = false)
    private LocalDateTime fechaAcceso;

    @Column(name = "HttpUserAgent", length = 200)
    private String httpUserAgent;

    @Column(name = "DireccionIp", length = 50)
    private String direccionIp;

    @Column(name = "Accion", length = 100)
    private String accion;

    @Column(name = "SistemaOperativo", length = 50)
    private String sistemaOperativo;

    @Column(name = "Dispositivo", length = 50)
    private String dispositivo;

    @Column(name = "Browser", length = 50)
    private String browser;

    @Column(name = "Sesion", length = 100)
    private String sesion;

    // Constructors
    public BitacoraAcceso() {
    }

    public BitacoraAcceso(String idUsuario, TipoAcceso tipoAcceso, String direccionIp, String accion) {
        this.idUsuario = idUsuario;
        this.tipoAcceso = tipoAcceso;
        this.fechaAcceso = LocalDateTime.now();
        this.direccionIp = direccionIp;
        this.accion = accion;
    }

    // Getters and Setters
    public Integer getIdBitacoraAcceso() {
        return idBitacoraAcceso;
    }

    public void setIdBitacoraAcceso(Integer idBitacoraAcceso) {
        this.idBitacoraAcceso = idBitacoraAcceso;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public TipoAcceso getTipoAcceso() {
        return tipoAcceso;
    }

    public void setTipoAcceso(TipoAcceso tipoAcceso) {
        this.tipoAcceso = tipoAcceso;
    }

    public LocalDateTime getFechaAcceso() {
        return fechaAcceso;
    }

    public void setFechaAcceso(LocalDateTime fechaAcceso) {
        this.fechaAcceso = fechaAcceso;
    }

    public String getHttpUserAgent() {
        return httpUserAgent;
    }

    public void setHttpUserAgent(String httpUserAgent) {
        this.httpUserAgent = httpUserAgent;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public String getSistemaOperativo() {
        return sistemaOperativo;
    }

    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getSesion() {
        return sesion;
    }

    public void setSesion(String sesion) {
        this.sesion = sesion;
    }
}
