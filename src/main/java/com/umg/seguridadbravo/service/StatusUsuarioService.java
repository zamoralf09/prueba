package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.StatusUsuario;
import com.umg.seguridadbravo.repository.StatusUsuarioRepository;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StatusUsuarioService {

    @Autowired
    private StatusUsuarioRepository statusUsuarioRepository;

    public List<StatusUsuario> findAll() {
        return statusUsuarioRepository.findAll();
    }

    public Optional<StatusUsuario> findById(Integer id) {
        return statusUsuarioRepository.findById(id);
    }

    public Optional<StatusUsuario> findByNombre(String nombre) {
        return statusUsuarioRepository.findByNombre(nombre);
    }

    public StatusUsuario save(StatusUsuario statusUsuario) {
        String currentUser = SecurityUtils.getCurrentUsername();
        if (currentUser == null) {
            currentUser = "system";
        }

        if (statusUsuario.getIdStatusUsuario() == null) {
            // New status
            statusUsuario.setFechaCreacion(LocalDateTime.now());
            statusUsuario.setUsuarioCreacion(currentUser);
        } else {
            // Existing status
            statusUsuario.setFechaModificacion(LocalDateTime.now());
            statusUsuario.setUsuarioModificacion(currentUser);
        }

        return statusUsuarioRepository.save(statusUsuario);
    }

    public void deleteById(Integer id) {
        if (!statusUsuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("El status de usuario no existe");
        }
        statusUsuarioRepository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return statusUsuarioRepository.existsByNombre(nombre);
    }

    public boolean existsById(Integer id) {
        return statusUsuarioRepository.existsById(id);
    }
}
