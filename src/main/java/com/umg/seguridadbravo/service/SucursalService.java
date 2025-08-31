package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Sucursal;
import com.umg.seguridadbravo.entity.Empresa;
import com.umg.seguridadbravo.repository.SucursalRepository;
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
public class SucursalService {

    @Autowired
    private SucursalRepository sucursalRepository;

    public List<Sucursal> findAll() {
        return sucursalRepository.findAll();
    }

    public Page<Sucursal> findAllPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return sucursalRepository.findAll(pageable);
    }

    public Optional<Sucursal> findById(Integer id) {
        return sucursalRepository.findById(id);
    }

    public List<Sucursal> findByEmpresa(Empresa empresa) {
        return sucursalRepository.findByEmpresa(empresa);
    }

    public List<Sucursal> findByEmpresaId(Integer empresaId) {
        return sucursalRepository.findByEmpresaIdEmpresa(empresaId);
    }

    public List<Sucursal> findByNombreContaining(String nombre) {
        return sucursalRepository.findByNombreContaining(nombre);
    }

    public Sucursal save(Sucursal sucursal) {
        String currentUser = SecurityUtils.getCurrentUsername();
        if (currentUser == null) {
            currentUser = "system";
        }

        if (sucursal.getIdSucursal() == null) {
            // New sucursal
            sucursal.setFechaCreacion(LocalDateTime.now());
            sucursal.setUsuarioCreacion(currentUser);
        } else {
            // Existing sucursal
            sucursal.setFechaModificacion(LocalDateTime.now());
            sucursal.setUsuarioModificacion(currentUser);
        }

        return sucursalRepository.save(sucursal);
    }

    public void deleteById(Integer id) {
        if (!sucursalRepository.existsById(id)) {
            throw new IllegalArgumentException("La sucursal no existe");
        }
        sucursalRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return sucursalRepository.existsById(id);
    }
}
