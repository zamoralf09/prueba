package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Menu;
import com.umg.seguridadbravo.entity.Modulo;
import com.umg.seguridadbravo.repository.MenuRepository;
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
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    public List<Menu> findAll() {
        return menuRepository.findByActivoTrueOrderByModuloIdAscOrdenAsc();
    }

    public Page<Menu> findAll(Pageable pageable) {
        return menuRepository.findByActivoTrue(pageable);
    }

    public Optional<Menu> findById(Long id) {
        return menuRepository.findByIdAndActivoTrue(id);
    }

    public List<Menu> findByModulo(Modulo modulo) {
        return menuRepository.findByModuloAndActivoTrueOrderByOrdenAsc(modulo);
    }

    public List<Menu> findByNombreContaining(String nombre) {
        return menuRepository.findByNombreContainingIgnoreCaseAndActivoTrueOrderByOrdenAsc(nombre);
    }

    public Menu save(Menu menu) {
        if (menu.getIdMenu() == null) {
            menu.setFechaCreacion(LocalDateTime.now());
        } else {
            menu.setFechaModificacion(LocalDateTime.now());
        }
        return menuRepository.save(menu);
    }

    public void deleteById(Integer id) {
        Optional<Menu> menu = menuRepository.findById(id);
        if (menu.isPresent()) {
            Menu m = menu.get();
            m.setFechaModificacion(LocalDateTime.now());
            menuRepository.save(m);
        }
    }

    public boolean existsByNombreAndModulo(String nombre, Modulo modulo) {
        return menuRepository.existsByNombreIgnoreCaseAndModuloAndActivoTrue(nombre, modulo);
    }

    public boolean existsByNombreAndModuloAndIdNot(String nombre, Modulo modulo, Long id) {
        return menuRepository.existsByNombreIgnoreCaseAndModuloAndActivoTrueAndIdNot(nombre, modulo, id);
    }

    public List<Menu> findMenusWithOpciones() {
        return menuRepository.findMenusWithOpciones();
    }
}
