package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Empresa;
import com.umg.seguridadbravo.repository.EmpresaRepository;
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
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Page<Empresa> findAllPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return empresaRepository.findAll(pageable);
    }

    public Optional<Empresa> findById(Integer id) {
        return empresaRepository.findById(id);
    }

    public Optional<Empresa> findByNit(String nit) {
        return empresaRepository.findByNit(nit);
    }

    public List<Empresa> findByNombreContaining(String nombre) {
        return empresaRepository.findByNombreContaining(nombre);
    }

    public Empresa save(Empresa empresa) {
        String currentUser = SecurityUtils.getCurrentUsername();
        if (currentUser == null) {
            currentUser = "system";
        }

        if (empresa.getIdEmpresa() == null) {
            // New empresa
            empresa.setFechaCreacion(LocalDateTime.now());
            empresa.setUsuarioCreacion(currentUser);
        } else {
            // Existing empresa
            empresa.setFechaModificacion(LocalDateTime.now());
            empresa.setUsuarioModificacion(currentUser);
        }

        return empresaRepository.save(empresa);
    }

    public void deleteById(Integer id) {
        if (!empresaRepository.existsById(id)) {
            throw new IllegalArgumentException("La empresa no existe");
        }
        empresaRepository.deleteById(id);
    }

    public boolean existsByNit(String nit) {
        return empresaRepository.existsByNit(nit);
    }

    public boolean existsById(Integer id) {
        return empresaRepository.existsById(id);
    }
}
