package com.umg.seguridadbravo.repository;

import com.umg.seguridadbravo.entity.BitacoraAcceso;
import com.umg.seguridadbravo.entity.TipoAcceso;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BitacoraAccesoRepository extends JpaRepository<BitacoraAcceso, Integer> {

    List<BitacoraAcceso> findByIdUsuario(String idUsuario);

    List<BitacoraAcceso> findByTipoAcceso(TipoAcceso tipoAcceso);

    @Query("SELECT b FROM BitacoraAcceso b WHERE b.idUsuario = ?1 ORDER BY b.fechaAcceso DESC")
    List<BitacoraAcceso> findByIdUsuarioOrderByFechaAccesoDesc(String idUsuario);

    @Query("SELECT b FROM BitacoraAcceso b WHERE b.fechaAcceso BETWEEN ?1 AND ?2 ORDER BY b.fechaAcceso DESC")
    List<BitacoraAcceso> findByFechaAccesoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query("SELECT b FROM BitacoraAcceso b WHERE b.idUsuario = :idUsuario AND b.fechaAcceso BETWEEN :fechaInicio AND :fechaFin")
    List<BitacoraAcceso> findByUsuarioAndFechaBetween(@Param("idUsuario") String idUsuario,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(b) FROM BitacoraAcceso b WHERE b.tipoAcceso.nombre = 'Acceso Concedido' AND b.fechaAcceso >= :fecha")
    long countAccesosConcedidosDesde(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT COUNT(b) FROM BitacoraAcceso b WHERE b.tipoAcceso.nombre LIKE '%Bloqueado%' AND b.fechaAcceso >= :fecha")
    long countAccesosBloqueadosDesde(@Param("fecha") LocalDateTime fecha);

    @Query("SELECT b FROM BitacoraAcceso b WHERE b.fechaAcceso > :since ORDER BY b.fechaAcceso DESC")
    Page<BitacoraAcceso> findByFechaAccesoAfterOrderByFechaAccesoDesc(LocalDateTime since, Pageable pageable);

    @Query("SELECT COUNT(b) FROM BitacoraAcceso b WHERE b.tipoAcceso.nombre = 'Bloqueado' AND b.fechaAcceso > :since")
    long countUsuariosBloqueadosRecientes(LocalDateTime since);

}
