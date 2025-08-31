package com.umg.seguridadbravo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @Column(name = "IdUsuario", length = 100)
    private String idUsuario;

    @NotBlank
    @Column(name = "Nombre", length = 100, nullable = false)
    private String nombre;

    @NotBlank
    @Column(name = "Apellido", length = 100, nullable = false)
    private String apellido;

    @NotNull
    @Column(name = "FechaNacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdStatusUsuario", nullable = false)
    private StatusUsuario statusUsuario;

    @NotBlank
    @Column(name = "Password", length = 100, nullable = false)
    private String password;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdGenero", nullable = false)
    private Genero genero;

    @Column(name = "UltimaFechaIngreso")
    private LocalDateTime ultimaFechaIngreso;

    @Column(name = "IntentosDeAcceso")
    private Integer intentosDeAcceso = 0;

    @Column(name = "SesionActual", length = 100)
    private String sesionActual;

    @Column(name = "UltimaFechaCambioPassword")
    private LocalDateTime ultimaFechaCambioPassword;

    @Email
    @Column(name = "CorreoElectronico", length = 100)
    private String correoElectronico;

    @Column(name = "RequiereCambiarPassword")
    private Integer requiereCambiarPassword = 0;

    @Lob
    @Column(name = "Fotografia")
    private byte[] fotografia;

    @Column(name = "TelefonoMovil", length = 30)
    private String telefonoMovil;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdSucursal", nullable = false)
    private Sucursal sucursal;

    @NotBlank
    @Column(name = "Pregunta", length = 200, nullable = false)
    private String pregunta;

    @NotBlank
    @Column(name = "Respuesta", length = 200, nullable = false)
    private String respuesta;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdRole", nullable = false)
    private Role role;

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

    // Constructors
    public Usuario() {
    }

    public Usuario(String idUsuario, String nombre, String apellido, LocalDate fechaNacimiento) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaCreacion = LocalDateTime.now();
        this.intentosDeAcceso = 0;
        this.requiereCambiarPassword = 0;
    }

    // Getters and Setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public StatusUsuario getStatusUsuario() {
        return statusUsuario;
    }

    public void setStatusUsuario(StatusUsuario statusUsuario) {
        this.statusUsuario = statusUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public LocalDateTime getUltimaFechaIngreso() {
        return ultimaFechaIngreso;
    }

    public void setUltimaFechaIngreso(LocalDateTime ultimaFechaIngreso) {
        this.ultimaFechaIngreso = ultimaFechaIngreso;
    }

    public Integer getIntentosDeAcceso() {
        return intentosDeAcceso;
    }

    public void setIntentosDeAcceso(Integer intentosDeAcceso) {
        this.intentosDeAcceso = intentosDeAcceso;
    }

    public String getSesionActual() {
        return sesionActual;
    }

    public void setSesionActual(String sesionActual) {
        this.sesionActual = sesionActual;
    }

    public LocalDateTime getUltimaFechaCambioPassword() {
        return ultimaFechaCambioPassword;
    }

    public void setUltimaFechaCambioPassword(LocalDateTime ultimaFechaCambioPassword) {
        this.ultimaFechaCambioPassword = ultimaFechaCambioPassword;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public Integer getRequiereCambiarPassword() {
        return requiereCambiarPassword;
    }

    public void setRequiereCambiarPassword(Integer requiereCambiarPassword) {
        this.requiereCambiarPassword = requiereCambiarPassword;
    }

    public byte[] getFotografia() {
        return fotografia;
    }

    public void setFotografia(byte[] fotografia) {
        this.fotografia = fotografia;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public Sucursal getSucursal() {
        return sucursal;
    }

    public void setSucursal(Sucursal sucursal) {
        this.sucursal = sucursal;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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

    // Utility methods
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public boolean isActivo() {
        return statusUsuario != null && "Activo".equals(statusUsuario.getNombre());
    }

    public boolean isBloqueado() {
        return statusUsuario != null && statusUsuario.getNombre().contains("Bloqueado");
    }
}
