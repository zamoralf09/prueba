package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    
    Optional<Role> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
    
    @Query("SELECT r FROM Role r JOIN FETCH r.roleOpciones ro JOIN FETCH ro.opcion WHERE r.idRole = ?1")
    Optional<Role> findByIdWithOpciones(Integer idRole);

    @Query("SELECT COUNT(u) FROM Role u")
    long countAll();
}
