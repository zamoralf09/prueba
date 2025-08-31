package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.entity.StatusUsuario;
import com.umg.seguridadbravo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, String> {
    
    Optional<Usuario> findByCorreoElectronico(String correoElectronico);
    
    List<Usuario> findByStatusUsuario(StatusUsuario statusUsuario);
    
    List<Usuario> findByRole(Role role);
    
    @Query("SELECT u FROM Usuario u WHERE u.nombre LIKE %?1% OR u.apellido LIKE %?1%")
    List<Usuario> findByNombreOrApellidoContaining(String searchTerm);
    
    @Query("SELECT u FROM Usuario u JOIN FETCH u.role r JOIN FETCH r.roleOpciones ro JOIN FETCH ro.opcion WHERE u.idUsuario = ?1")
    Optional<Usuario> findByIdUsuarioWithPermissions(String idUsuario);
    
    @Modifying
    @Query("UPDATE Usuario u SET u.intentosDeAcceso = u.intentosDeAcceso + 1 WHERE u.idUsuario = :idUsuario")
    void incrementarIntentosAcceso(@Param("idUsuario") String idUsuario);
    
    @Modifying
    @Query("UPDATE Usuario u SET u.intentosDeAcceso = 0, u.ultimaFechaIngreso = :fechaIngreso WHERE u.idUsuario = :idUsuario")
    void resetearIntentosYActualizarIngreso(@Param("idUsuario") String idUsuario, @Param("fechaIngreso") LocalDateTime fechaIngreso);
    
    @Modifying
    @Query("UPDATE Usuario u SET u.sesionActual = :sesion WHERE u.idUsuario = :idUsuario")
    void actualizarSesion(@Param("idUsuario") String idUsuario, @Param("sesion") String sesion);

    @Query("SELECT COUNT(u) FROM Usuario u")
    long countAll();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.activo = true")
    long countByActivoTrue();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.statusUsuario.nombre = 'Activo'")
    long countUsuariosActivos();

    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.statusUsuario.nombre = 'Bloqueado'")
    long countUsuariosBloqueados();
}
