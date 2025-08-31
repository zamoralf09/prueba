package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.Sucursal;
import com.umg.seguridadbravo.entity.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SucursalRepository extends JpaRepository<Sucursal, Integer> {
    
    List<Sucursal> findByEmpresa(Empresa empresa);
    
    List<Sucursal> findByEmpresaIdEmpresa(Integer idEmpresa);
    
    @Query("SELECT s FROM Sucursal s WHERE s.nombre LIKE %?1%")
    List<Sucursal> findByNombreContaining(String nombre);
}
