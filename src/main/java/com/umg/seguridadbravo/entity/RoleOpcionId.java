package com.umg.seguridadbravo.entity;

import java.io.Serializable;
import java.util.Objects;

public class RoleOpcionId implements Serializable {

    private Integer role;
    private Integer opcion;

    // Constructors
    public RoleOpcionId() {
    }

    public RoleOpcionId(Integer role, Integer opcion) {
        this.role = role;
        this.opcion = opcion;
    }

    // Getters and Setters
    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getOpcion() {
        return opcion;
    }

    public void setOpcion(Integer opcion) {
        this.opcion = opcion;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RoleOpcionId that = (RoleOpcionId) o;
        return Objects.equals(role, that.role) && Objects.equals(opcion, that.opcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(role, opcion);
    }
}
