package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {
    
    Optional<Genero> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}
