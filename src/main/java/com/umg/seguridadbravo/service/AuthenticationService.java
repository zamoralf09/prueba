package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.entity.BitacoraAcceso;
import com.umg.seguridadbravo.entity.TipoAcceso;
import com.umg.seguridadbravo.repository.UsuarioRepository;
import com.umg.seguridadbravo.repository.BitacoraAccesoRepository;
import com.umg.seguridadbravo.repository.TipoAccesoRepository;
import com.umg.seguridadbravo.security.MD5PasswordEncoder;
import com.umg.seguridadbravo.security.PasswordPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BitacoraAccesoRepository bitacoraAccesoRepository;
    
    @Autowired
    private TipoAccesoRepository tipoAccesoRepository;
    
    @Autowired
    private MD5PasswordEncoder passwordEncoder;
    
    @Autowired
    private PasswordPolicyValidator passwordPolicyValidator;
    
    @Autowired
    private EmailService emailService;

    public boolean authenticate(String username, String password, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
            
        } catch (AuthenticationException e) {
            logFailedAuthentication(username, e.getMessage(), request);
            return false;
        }
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verify current password
        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            return false;
        }
        
        // Validate new password against company policy
        List<String> validationErrors = passwordPolicyValidator.validatePassword(
            newPassword, usuario.getSucursal().getEmpresa()
        );
        
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no cumple con las políticas: " + 
                String.join(", ", validationErrors));
        }
        
        // Update password
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        
        usuarioRepository.save(usuario);
        
        logPasswordChange(username);
        return true;
    }

    public boolean resetPassword(String username, String securityAnswer, String newPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Verify security answer
        if (!usuario.getRespuesta().equalsIgnoreCase(securityAnswer.trim())) {
            return false;
        }
        
        // Validate new password
        List<String> validationErrors = passwordPolicyValidator.validatePassword(
            newPassword, usuario.getSucursal().getEmpresa()
        );
        
        if (!validationErrors.isEmpty()) {
            throw new IllegalArgumentException("La nueva contraseña no cumple con las políticas: " + 
                String.join(", ", validationErrors));
        }
        
        // Reset password
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setUltimaFechaCambioPassword(LocalDateTime.now());
        usuario.setRequiereCambiarPassword(0);
        usuario.setIntentosDeAcceso(0);
        
        usuarioRepository.save(usuario);
        
        logPasswordReset(username);
        return true;
    }

    public String generatePasswordResetToken(String username) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        
        if (usuarioOpt.isEmpty()) {
            return null;
        }
        
        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();
        
        // Store token in session or cache (simplified approach)
        // In production, use Redis or database table for tokens
        usuario.setSesionActual(token);
        usuarioRepository.save(usuario);
        
        return token;
    }

    public boolean unlockUser(String username, String adminUsername) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        
        if (usuarioOpt.isEmpty()) {
            return false;
        }
        
        Usuario usuario = usuarioOpt.get();
        
        // Reset attempts and unlock
        usuario.setIntentosDeAcceso(0);
        
        // Set to active status
        usuario.getStatusUsuario().setNombre("Activo");
        
        usuarioRepository.save(usuario);
        
        logUserUnlock(username, adminUsername);
        return true;
    }

    public void logUserActivity(String username, String action, HttpServletRequest request) {
        TipoAcceso tipoAcceso = tipoAccesoRepository.findByNombre("Acceso Concedido")
                .orElse(null);
        
        if (tipoAcceso != null) {
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setIdUsuario(username);
            bitacora.setTipoAcceso(tipoAcceso);
            bitacora.setFechaAcceso(LocalDateTime.now());
            bitacora.setAccion(action);
            
            if (request != null) {
                bitacora.setDireccionIp(getClientIpAddress(request));
                bitacora.setHttpUserAgent(request.getHeader("User-Agent"));
                bitacora.setSesion(request.getSession().getId());
            }
            
            bitacoraAccesoRepository.save(bitacora);
        }
    }

    public void logout(String username, HttpServletRequest request) {
        // Clear user session
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setSesionActual(null);
            usuarioRepository.save(usuario);
        }
        
        // Log logout activity
        logUserActivity(username, "LOGOUT", request);
        
        // Clear security context
        SecurityContextHolder.clearContext();
        
        // Invalidate session
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }
    }

    public boolean isPasswordExpired(Usuario usuario) {
        if (usuario.getUltimaFechaCambioPassword() == null) {
            return true; // Force password change if never changed
        }
        
        Integer diasCaducidad = usuario.getSucursal().getEmpresa().getPasswordCantidadCaducidadDias();
        if (diasCaducidad == null) {
            return false; // No expiration policy
        }
        
        long daysSinceLastChange = java.time.temporal.ChronoUnit.DAYS.between(
            usuario.getUltimaFechaCambioPassword().toLocalDate(),
            java.time.LocalDate.now()
        );
        
        return daysSinceLastChange >= diasCaducidad;
    }

    public int getDaysUntilPasswordExpires(Usuario usuario) {
        if (usuario.getUltimaFechaCambioPassword() == null) {
            return 0;
        }
        
        Integer diasCaducidad = usuario.getSucursal().getEmpresa().getPasswordCantidadCaducidadDias();
        if (diasCaducidad == null) {
            return Integer.MAX_VALUE; // No expiration
        }
        
        long daysSinceLastChange = java.time.temporal.ChronoUnit.DAYS.between(
            usuario.getUltimaFechaCambioPassword().toLocalDate(),
            java.time.LocalDate.now()
        );
        
        return Math.max(0, diasCaducidad.intValue() - (int) daysSinceLastChange);
    }

    private void logFailedAuthentication(String username, String reason, HttpServletRequest request) {
        TipoAcceso tipoAcceso = tipoAccesoRepository
            .findByNombre("Bloqueado - Password incorrecto/Numero de intentos exedidos")
            .orElse(null);
        
        if (tipoAcceso != null) {
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setIdUsuario(username);
            bitacora.setTipoAcceso(tipoAcceso);
            bitacora.setFechaAcceso(LocalDateTime.now());
            bitacora.setAccion("AUTHENTICATION_FAILED: " + reason);
            
            if (request != null) {
                bitacora.setDireccionIp(getClientIpAddress(request));
                bitacora.setHttpUserAgent(request.getHeader("User-Agent"));
            }
            
            bitacoraAccesoRepository.save(bitacora);
        }
    }

    private void logPasswordChange(String username) {
        logUserActivity(username, "PASSWORD_CHANGED", null);
    }

    private void logPasswordReset(String username) {
        logUserActivity(username, "PASSWORD_RESET", null);
    }

    private void logUserUnlock(String username, String adminUsername) {
        TipoAcceso tipoAcceso = tipoAccesoRepository.findByNombre("Acceso Concedido")
                .orElse(null);
        
        if (tipoAcceso != null) {
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setIdUsuario(username);
            bitacora.setTipoAcceso(tipoAcceso);
            bitacora.setFechaAcceso(LocalDateTime.now());
            bitacora.setAccion("USER_UNLOCKED_BY: " + adminUsername);
            
            bitacoraAccesoRepository.save(bitacora);
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            return request.getRemoteAddr();
        } else {
            return xForwardedForHeader.split(",")[0];
        }
    }
}
