package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Modulo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Integer> {
    
    Optional<Modulo> findByNombre(String nombre);
    
    @Query("SELECT m FROM Modulo m ORDER BY m.ordenMenu ASC")
    List<Modulo> findAllOrderByOrdenMenu();
    
    boolean existsByNombre(String nombre);

    List<Modulo> findByActivoTrueOrderByOrdenAsc();

    Page<Modulo> findByActivoTrue(Pageable pageable);

    Optional<Modulo> findByIdAndActivoTrue(Long id);

    List<Modulo> findByNombreContainingIgnoreCaseAndActivoTrueOrderByOrdenAsc(String nombre);

    boolean existsByNombreIgnoreCaseAndActivoTrue(String nombre);

    boolean existsByNombreIgnoreCaseAndActivoTrueAndIdNot(String nombre, Long id);

    @Query("SELECT DISTINCT m FROM Modulo m JOIN FETCH m.menus WHERE m.activo = true")
    List<Modulo> findModulosWithMenus();

}
