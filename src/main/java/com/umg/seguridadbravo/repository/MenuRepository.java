package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Menu;
import com.umg.seguridadbravo.entity.Modulo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    
    List<Menu> findByModulo(Modulo modulo);
    
    List<Menu> findByModuloIdModulo(Integer idModulo);
    
    @Query("SELECT m FROM Menu m WHERE m.modulo.idModulo = ?1 ORDER BY m.ordenMenu ASC")
    List<Menu> findByModuloOrderByOrdenMenu(Integer idModulo);
    
    Optional<Menu> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);

    List<Menu> findByActivoTrueOrderByModuloIdAscOrdenAsc();

    Page<Menu> findByActivoTrue(Pageable pageable);

    Optional<Menu> findByIdAndActivoTrue(Long id);

    List<Menu> findByModuloAndActivoTrueOrderByOrdenAsc(Modulo modulo);

    List<Menu> findByNombreContainingIgnoreCaseAndActivoTrueOrderByOrdenAsc(String nombre);

    boolean existsByNombreIgnoreCaseAndModuloAndActivoTrue(String nombre, Modulo modulo);

    boolean existsByNombreIgnoreCaseAndModuloAndActivoTrueAndIdNot(String nombre, Modulo modulo, Long id);

    @Query("SELECT DISTINCT m FROM Menu m JOIN FETCH m.opciones WHERE m.activo = true")
    List<Menu> findMenusWithOpciones();

}
