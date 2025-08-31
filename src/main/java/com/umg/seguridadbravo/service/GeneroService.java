package com.umg.seguridadbravo.service; 

import com.umg.seguridadbravo.entity.Genero;
import com.umg.seguridadbravo.repository.GeneroRepository;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GeneroService {

    @Autowired
    private GeneroRepository generoRepository;

    public List<Genero> findAll() {
        return generoRepository.findAll();
    }

    public Optional<Genero> findById(Integer id) {
        return generoRepository.findById(id);
    }

    public Optional<Genero> findByNombre(String nombre) {
        return generoRepository.findByNombre(nombre);
    }

    public Genero save(Genero genero) {
        String currentUser = SecurityUtils.getCurrentUsername();
        if (currentUser == null) {
            currentUser = "system";
        }

        if (genero.getIdGenero() == null) {
            // New genero
            genero.setFechaCreacion(LocalDateTime.now());
            genero.setUsuarioCreacion(currentUser);
        } else {
            // Existing genero
            genero.setFechaModificacion(LocalDateTime.now());
            genero.setUsuarioModificacion(currentUser);
        }

        return generoRepository.save(genero);
    }

    public void deleteById(Integer id) {
        if (!generoRepository.existsById(id)) {
            throw new IllegalArgumentException("El género no existe");
        }
        generoRepository.deleteById(id);
    }

    public boolean existsByNombre(String nombre) {
        return generoRepository.existsByNombre(nombre);
    }

    public boolean existsById(Integer id) {
        return generoRepository.existsById(id);
    }
}
