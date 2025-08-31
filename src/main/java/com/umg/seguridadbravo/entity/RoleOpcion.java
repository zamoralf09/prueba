package com.umg.seguridadbravo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "ROLE_OPCION")
@IdClass(RoleOpcionId.class)
public class RoleOpcion {

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRole", nullable = false)
    private Role role;

    @Id
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdOpcion", nullable = false)
    private Opcion opcion;

    @NotNull
    @Column(name = "Alta", nullable = false)
    private Boolean alta;

    @NotNull
    @Column(name = "Baja", nullable = false)
    private Boolean baja;

    @NotNull
    @Column(name = "Cambio", nullable = false)
    private Boolean cambio;

    @NotNull
    @Column(name = "Imprimir", nullable = false)
    private Boolean imprimir;

    @NotNull
    @Column(name = "Exportar", nullable = false)
    private Boolean exportar;

    @NotNull
    @Column(name = "FechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @NotNull
    @Column(name = "UsuarioCreacion", length = 100, nullable = false)
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion", length = 100)
    private String usuarioModificacion;

    // Constructors
    public RoleOpcion() {
    }

    public RoleOpcion(Role role, Opcion opcion, Boolean alta, Boolean baja, Boolean cambio, Boolean imprimir,
            Boolean exportar) {
        this.role = role;
        this.opcion = opcion;
        this.alta = alta;
        this.baja = baja;
        this.cambio = cambio;
        this.imprimir = imprimir;
        this.exportar = exportar;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters and Setters
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Opcion getOpcion() {
        return opcion;
    }

    public void setOpcion(Opcion opcion) {
        this.opcion = opcion;
    }

    public Boolean getAlta() {
        return alta;
    }

    public void setAlta(Boolean alta) {
        this.alta = alta;
    }

    public Boolean getBaja() {
        return baja;
    }

    public void setBaja(Boolean baja) {
        this.baja = baja;
    }

    public Boolean getCambio() {
        return cambio;
    }

    public void setCambio(Boolean cambio) {
        this.cambio = cambio;
    }

    public Boolean getImprimir() {
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public Boolean getExportar() {
        return exportar;
    }

    public void setExportar(Boolean exportar) {
        this.exportar = exportar;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(String usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }
}
