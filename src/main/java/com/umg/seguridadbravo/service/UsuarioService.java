package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.*;
import com.umg.seguridadbravo.repository.*;
import com.umg.seguridadbravo.security.MD5PasswordEncoder;
import com.umg.seguridadbravo.security.PasswordPolicyValidator;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private StatusUsuarioRepository statusUsuarioRepository;
    
    @Autowired
    private GeneroRepository generoRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private SucursalRepository sucursalRepository;
    
    @Autowired
    private MD5PasswordEncoder passwordEncoder;
    
    @Autowired
    private PasswordPolicyValidator passwordPolicyValidator;
    
    @Autowired
    private AuthenticationService authenticationService;

    public long countAll() {
        return usuarioRepository.countAll();
    }

    public long countByActivoTrue() {
        return usuarioRepository.countByActivoTrue();
    }

    public long countUsuariosActivos() {
        return usuarioRepository.countUsuariosActivos();
    }

    public long countUsuariosBloqueados() {
        return usuarioRepository.countUsuariosBloqueados();
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Page<Usuario> findAllPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                   Sort.by(sortBy).descending() : 
                   Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        return usuarioRepository.findAll(pageable);
    }

    public Optional<Usuario> findById(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByIdWithPermissions(String id) {
        return usuarioRepository.findByIdUsuarioWithPermissions(id);
    }

    public List<Usuario> findByNombreOrApellido(String searchTerm) {
        return usuarioRepository.findByNombreOrApellidoContaining(searchTerm);
    }

    public List<Usuario> findByStatus(StatusUsuario status) {
        return usuarioRepository.findByStatusUsuario(status);
    }

    public List<Usuario> findByRole(Role role) {
        return usuarioRepository.findByRole(role);
    }

    public Usuario save(Usuario usuario) {
        return save(usuario, null);
    }

    public Usuario save(Usuario usuario, MultipartFile fotografiaFile) {
        // Set audit fields
        String currentUser = SecurityUtils.getCurrentUsername();
        if (currentUser == null) {
            currentUser = "system";
        }

        if (usuario.getIdUsuario() == null || !usuarioRepository.existsById(usuario.getIdUsuario())) {
            // New user
            usuario.setFechaCreacion(LocalDateTime.now());
            usuario.setUsuarioCreacion(currentUser);
            
            // Set default values
            if (usuario.getIntentosDeAcceso() == null) {
                usuario.setIntentosDeAcceso(0);
            }
            if (usuario.getRequiereCambiarPassword() == null) {
                usuario.setRequiereCambiarPassword(1); // Force password change on first login
            }
        } else {
            // Existing user
            usuario.setFechaModificacion(LocalDateTime.now());
            usuario.setUsuarioModificacion(currentUser);
        }

        // Handle photograph upload
        if (fotografiaFile != null && !fotografiaFile.isEmpty()) {
            try {
                usuario.setFotografia(fotografiaFile.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error al procesar la fotografía", e);
            }
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario create(Usuario usuario, String password) {
        // Validate that user doesn't exist
        if (usuarioRepository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        // Validate password policy
        List<String> validationErrors = passwordPolicyValidator.validatePassword(
            password, usuario.getSucursal().getEmpresa()
        );
        
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no cumple con las políticas: " + 
                String.join(", ", validationErrors));
        }

        // Encode password
        usuario.setPassword(passwordEncoder.encode(password));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());

        return save(usuario);
    }

    public Usuario update(Usuario usuario) {
        if (!usuarioRepository.existsById(usuario.getIdUsuario())) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        return save(usuario);
    }

    public void deleteById(String id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("El usuario no existe");
        }

        // Don't actually delete, just deactivate
        Usuario usuario = usuarioRepository.findById(id).get();
        StatusUsuario statusInactivo = statusUsuarioRepository.findByNombre("Inactivo")
                .orElseThrow(() -> new RuntimeException("Status 'Inactivo' no encontrado"));
        
        usuario.setStatusUsuario(statusInactivo);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario.setUsuarioModificacion(SecurityUtils.getCurrentUsername());
        
        usuarioRepository.save(usuario);

        // Log user deactivation
        authenticationService.logUserActivity(id, "USER_DEACTIVATED", null);
    }

    public boolean changePassword(String userId, String newPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();

        // Validate password policy
        List<String> validationErrors = passwordPolicyValidator.validatePassword(
            newPassword, usuario.getSucursal().getEmpresa()
        );
        
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no cumple con las políticas: " + 
                String.join(", ", validationErrors));
        }

        // actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario.setUsuarioModificacion(SecurityUtils.getCurrentUsername());

        usuarioRepository.save(usuario);

        // cambiar contraseña
        authenticationService.logUserActivity(userId, "PASSWORD_CHANGED_BY_ADMIN", null);
        
        return true;
    }

    public boolean unlockUser(String userId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        
        // reiniciar intentos de acceso y cambiar status
        usuario.setIntentosDeAcceso(0);
        StatusUsuario statusActivo = statusUsuarioRepository.findByNombre("Activo")
                .orElseThrow(() -> new RuntimeException("Status 'Activo' no encontrado"));
        
        usuario.setStatusUsuario(statusActivo);
        usuario.setFechaModificacion(LocalDateTime.now());
        usuario.setUsuarioModificacion(SecurityUtils.getCurrentUsername());

        usuarioRepository.save(usuario);

        // bloquar acceso
        authenticationService.logUserActivity(userId, "USER_UNLOCKED_BY_ADMIN", null);
        
        return true;
    }

    public byte[] getUserPhoto(String userId) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        return usuarioOpt.map(Usuario::getFotografia).orElse(null);
    }

    public boolean updateUserPhoto(String userId, MultipartFile photoFile) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(userId);
        if (usuarioOpt.isEmpty()) {
            return false;
        }

        Usuario usuario = usuarioOpt.get();
        
        try {
            usuario.setFotografia(photoFile.getBytes());
            usuario.setFechaModificacion(LocalDateTime.now());
            usuario.setUsuarioModificacion(SecurityUtils.getCurrentUsername());
            
            usuarioRepository.save(usuario);
            return true;
            
        } catch (IOException e) {
            throw new RuntimeException("Error al procesar la fotografía", e);
        }
    }

    public boolean existsById(String id) {
        return usuarioRepository.existsById(id);
    }
}
