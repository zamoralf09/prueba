package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Opcion;
import com.umg.seguridadbravo.entity.Menu;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Integer> {
    
    List<Opcion> findByMenu(Menu menu);
    
    List<Opcion> findByMenuIdMenu(Integer idMenu);
    
    @Query("SELECT o FROM Opcion o WHERE o.menu.idMenu = ?1 ORDER BY o.ordenMenu ASC")
    List<Opcion> findByMenuOrderByOrdenMenu(Integer idMenu);
    
    Optional<Opcion> findByNombre(String nombre);
    
    Optional<Opcion> findByPagina(String pagina);
    
    @Query("SELECT o FROM Opcion o JOIN o.roleOpciones ro WHERE ro.role.idRole = ?1 AND ro.alta = true")
    List<Opcion> findOpcionesByRoleWithAlta(Integer idRole);
    
    boolean existsByNombre(String nombre);
    
    boolean existsByPagina(String pagina);

    List<Opcion> findByActivoTrueOrderByMenuIdAscOrdenAsc();

    Page<Opcion> findByActivoTrue(Pageable pageable);

    Optional<Opcion> findByIdAndActivoTrue(Long id);

    List<Opcion> findByMenuAndActivoTrueOrderByOrdenAsc(Menu menu);

    List<Opcion> findByNombreContainingIgnoreCaseAndActivoTrueOrderByOrdenAsc(String nombre);

    boolean existsByNombreIgnoreCaseAndMenuAndActivoTrue(String nombre, Menu menu);

    boolean existsByNombreIgnoreCaseAndMenuAndActivoTrueAndIdNot(String nombre, Menu menu, Long id);

    @Query("SELECT o FROM Opcion o JOIN o.roleOpciones ro WHERE ro.role.id = ?1 AND ro.activo = true")
    List<Opcion> findOpcionesByRoleId(Integer roleId);

}
