package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Menu;
import com.umg.seguridadbravo.entity.Opcion;
import com.umg.seguridadbravo.repository.OpcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OpcionService {

    @Autowired
    private OpcionRepository opcionRepository;

    public List<Opcion> findAll() {
        return opcionRepository.findByActivoTrueOrderByMenuIdAscOrdenAsc();
    }

    public Page<Opcion> findAll(Pageable pageable) {
        return opcionRepository.findByActivoTrue(pageable);
    }

    public Optional<Opcion> findById(Long id) {
        return opcionRepository.findByIdAndActivoTrue(id);
    }

    public List<Opcion> findByMenu(Menu menu) {
        return opcionRepository.findByMenuAndActivoTrueOrderByOrdenAsc(menu);
    }

    public List<Opcion> findByNombreContaining(String nombre) {
        return opcionRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrderByOrdenAsc(nombre);
    }

    public Opcion save(Opcion opcion) {
        if (opcion.getIdOpcion() == null) {
            opcion.setFechaCreacion(LocalDateTime.now());
        } else {
            opcion.setFechaModificacion(LocalDateTime.now());
        }
        return opcionRepository.save(opcion);
    }

    public void deleteById(Integer id) {
        Optional<Opcion> opcion = opcionRepository.findById(id);
        if (opcion.isPresent()) {
            Opcion o = opcion.get();
            o.setFechaModificacion(LocalDateTime.now());
            opcionRepository.save(o);
        }
    }

    public boolean existsByNombreAndMenu(String nombre, Menu menu) {
        return opcionRepository.existsByNombreIgnoreCaseAndMenuAndActivoTrue(nombre, menu);
    }

    public boolean existsByNombreAndMenuAndIdNot(String nombre, Menu menu, Long id) {
        return opcionRepository.existsByNombreIgnoreCaseAndMenuAndActivoTrueAndIdNot(nombre, menu, id);
    }

    public List<Opcion> findOpcionesByRoleId(Integer roleId) {
        return opcionRepository.findOpcionesByRoleId(roleId);
    }
}
