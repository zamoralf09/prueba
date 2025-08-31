package com.umg.seguridadbravo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "EMPRESA")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdEmpresa")
    private Integer idEmpresa;

    @NotBlank
    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "Direccion", length = 200, nullable = false)
    private String direccion;

    @NotBlank
    @Column(name = "Nit", length = 20, nullable = false)
    private String nit;

    @Column(name = "PasswordCantidadMayusculas")
    private Integer passwordCantidadMayusculas;

    @Column(name = "PasswordCantidadMinusculas")
    private Integer passwordCantidadMinusculas;

    @Column(name = "PasswordCantidadCaracteresEspeciales")
    private Integer passwordCantidadCaracteresEspeciales;

    @Column(name = "PasswordCantidadCaducidadDias")
    private Integer passwordCantidadCaducidadDias;

    @Column(name = "PasswordLargo")
    private Integer passwordLargo;

    @Column(name = "PasswordIntentosAntesDeBloquear")
    private Integer passwordIntentosAntesDeBloquear;

    @Column(name = "PasswordCantidadNumeros")
    private Integer passwordCantidadNumeros;

    @Column(name = "PasswordCantidadPreguntasValidar")
    private Integer passwordCantidadPreguntasValidar;

    @NotNull
    @Column(name = "FechaCreacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @NotBlank
    @Column(name = "UsuarioCreacion", length = 100, nullable = false)
    private String usuarioCreacion;

    @Column(name = "FechaModificacion")
    private LocalDateTime fechaModificacion;

    @Column(name = "UsuarioModificacion", length = 100)
    private String usuarioModificacion;

    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Sucursal> sucursales;

    // Constructors
    public Empresa() {
    }

    public Empresa(String nombre, String direccion, String nit) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.nit = nit;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public Integer getPasswordCantidadMayusculas() {
        return passwordCantidadMayusculas;
    }

    public void setPasswordCantidadMayusculas(Integer passwordCantidadMayusculas) {
        this.passwordCantidadMayusculas = passwordCantidadMayusculas;
    }

    public Integer getPasswordCantidadMinusculas() {
        return passwordCantidadMinusculas;
    }

    public void setPasswordCantidadMinusculas(Integer passwordCantidadMinusculas) {
        this.passwordCantidadMinusculas = passwordCantidadMinusculas;
    }

    public Integer getPasswordCantidadCaracteresEspeciales() {
        return passwordCantidadCaracteresEspeciales;
    }

    public void setPasswordCantidadCaracteresEspeciales(Integer passwordCantidadCaracteresEspeciales) {
        this.passwordCantidadCaracteresEspeciales = passwordCantidadCaracteresEspeciales;
    }

    public Integer getPasswordCantidadCaducidadDias() {
        return passwordCantidadCaducidadDias;
    }

    public void setPasswordCantidadCaducidadDias(Integer passwordCantidadCaducidadDias) {
        this.passwordCantidadCaducidadDias = passwordCantidadCaducidadDias;
    }

    public Integer getPasswordLargo() {
        return passwordLargo;
    }

    public void setPasswordLargo(Integer passwordLargo) {
        this.passwordLargo = passwordLargo;
    }

    public Integer getPasswordIntentosAntesDeBloquear() {
        return passwordIntentosAntesDeBloquear;
    }

    public void setPasswordIntentosAntesDeBloquear(Integer passwordIntentosAntesDeBloquear) {
        this.passwordIntentosAntesDeBloquear = passwordIntentosAntesDeBloquear;
    }

    public Integer getPasswordCantidadNumeros() {
        return passwordCantidadNumeros;
    }

    public void setPasswordCantidadNumeros(Integer passwordCantidadNumeros) {
        this.passwordCantidadNumeros = passwordCantidadNumeros;
    }

    public Integer getPasswordCantidadPreguntasValidar() {
        return passwordCantidadPreguntasValidar;
    }

    public void setPasswordCantidadPreguntasValidar(Integer passwordCantidadPreguntasValidar) {
        this.passwordCantidadPreguntasValidar = passwordCantidadPreguntasValidar;
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

    public List<Sucursal> getSucursales() {
        return sucursales;
    }

    public void setSucursales(List<Sucursal> sucursales) {
        this.sucursales = sucursales;
    }
}
