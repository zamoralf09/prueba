package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Modulo;
import com.umg.seguridadbravo.repository.ModuloRepository;
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
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;

    public List<Modulo> findAll() {
        return moduloRepository.findByActivoTrueOrderByOrdenAsc();
    }

    public Page<Modulo> findAll(Pageable pageable) {
        return moduloRepository.findByActivoTrue(pageable);
    }

    public Optional<Modulo> findById(Long id) {
        return moduloRepository.findByIdAndActivoTrue(id);
    }

    public List<Modulo> findByNombreContaining(String nombre) {
        return moduloRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrderByOrdenAsc(nombre);
    }

    public Modulo save(Modulo modulo) {
        if (modulo.getIdModulo() == null) {
            modulo.setFechaCreacion(LocalDateTime.now());
        } else {
            modulo.setFechaModificacion(LocalDateTime.now());
        }
        return moduloRepository.save(modulo);
    }

    public void deleteById(Integer id) {
        Optional<Modulo> modulo = moduloRepository.findById(id);
        if (modulo.isPresent()) {
            Modulo m = modulo.get();
            m.setFechaModificacion(LocalDateTime.now());
            moduloRepository.save(m);
        }
    }

    public boolean existsByNombre(String nombre) {
        return moduloRepository.existsByNombreIgnoreCaseAndActivoTrue(nombre);
    }

    public boolean existsByNombreAndIdNot(String nombre, Long id) {
        return moduloRepository.existsByNombreIgnoreCaseAndActivoTrueAndIdNot(nombre, id);
    }

    public List<Modulo> findModulosWithMenus() {
        return moduloRepository.findModulosWithMenus();
    }
}
