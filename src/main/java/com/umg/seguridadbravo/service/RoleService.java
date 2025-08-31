package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Role;
import com.umg.seguridadbravo.repository.RoleRepository;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public long countAll() {
        return roleRepository.countAll();
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Page<Role> findAllPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return roleRepository.findAll(pageable);
    }

    public Page<Role> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    public Optional<Role> findById(Integer id) {
        return roleRepository.findById(id);
    }

    public Optional<Role> findByIdWithOpciones(Integer id) {
        return roleRepository.findByIdWithOpciones(id);
    }

    public Optional<Role> findByNombre(String nombre) {
        return roleRepository.findByNombre(nombre);
    }

    public Role save(Role role) {
        String currentUser = SecurityUtils.getCurrentUsername();
        if (currentUser == null) {
            currentUser = "system";
        }

        if (role.getIdRole() == null) {
            role.setFechaCreacion(LocalDateTime.now());
            role.setUsuarioCreacion(currentUser);
        } else {
            role.setFechaModificacion(LocalDateTime.now());
            role.setUsuarioModificacion(currentUser);
        }

        return roleRepository.save(role);
    }

    public void deleteById(Integer id) {
        if (!roleRepository.existsById(id)) {
            throw new IllegalArgumentException("El rol no existe");
        }
        roleRepository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return roleRepository.existsByNombre(nombre);
    }

    public boolean existsById(Integer id) {
        return roleRepository.existsById(id);
    }
}
