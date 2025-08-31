package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.RoleOpcion;
import com.umg.seguridadbravo.entity.RoleOpcionId;
import com.umg.seguridadbravo.entity.Role;
import com.umg.seguridadbravo.entity.Opcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoleOpcionRepository extends JpaRepository<RoleOpcion, RoleOpcionId> {
    
    List<RoleOpcion> findByRole(Role role);
    
    List<RoleOpcion> findByOpcion(Opcion opcion);
    
    List<RoleOpcion> findByRoleIdRole(Integer idRole);
    
    List<RoleOpcion> findByOpcionIdOpcion(Integer idOpcion);
    
    @Query("SELECT ro FROM RoleOpcion ro WHERE ro.role.idRole = ?1 AND ro.alta = true")
    List<RoleOpcion> findByRoleWithAlta(Integer idRole);
    
    @Query("SELECT ro FROM RoleOpcion ro WHERE ro.role.idRole = ?1 AND ro.baja = true")
    List<RoleOpcion> findByRoleWithBaja(Integer idRole);
    
    @Query("SELECT ro FROM RoleOpcion ro WHERE ro.role.idRole = ?1 AND ro.cambio = true")
    List<RoleOpcion> findByRoleWithCambio(Integer idRole);
    
    Optional<RoleOpcion> findByRoleIdRoleAndOpcionIdOpcion(Integer idRole, Integer idOpcion);

    List<RoleOpcion> findByRoleAndActivoTrue(Role role);

    boolean existsByRoleAndOpcionIdAndActivoTrue(Role role, Long opcionId);

    boolean existsByRoleAndOpcionNombreAndActivoTrue(Role role, String opcionNombre);

    Optional<RoleOpcion> findByRoleAndOpcionAndActivoTrue(Role role, Opcion opcion);

}
