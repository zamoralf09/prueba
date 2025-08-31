package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Integer> {
    
    Optional<Empresa> findByNit(String nit);
    
    @Query("SELECT e FROM Empresa e WHERE e.nombre LIKE %?1%")
    java.util.List<Empresa> findByNombreContaining(String nombre);
    
    boolean existsByNit(String nit);
}
