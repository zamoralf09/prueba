package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.TipoAcceso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TipoAccesoRepository extends JpaRepository<TipoAcceso, Integer> {
    
    Optional<TipoAcceso> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}
