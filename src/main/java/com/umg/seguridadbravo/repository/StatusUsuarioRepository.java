package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.StatusUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface StatusUsuarioRepository extends JpaRepository<StatusUsuario, Integer> {
    
    Optional<StatusUsuario> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}
