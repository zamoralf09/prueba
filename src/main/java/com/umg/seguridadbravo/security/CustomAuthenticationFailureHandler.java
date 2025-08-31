package com.umg.seguridadbravo.security;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.entity.BitacoraAcceso;
import com.umg.seguridadbravo.entity.TipoAcceso;
import com.umg.seguridadbravo.entity.StatusUsuario;
import com.umg.seguridadbravo.repository.UsuarioRepository;
import com.umg.seguridadbravo.repository.BitacoraAccesoRepository;
import com.umg.seguridadbravo.repository.TipoAccesoRepository;
import com.umg.seguridadbravo.repository.StatusUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BitacoraAccesoRepository bitacoraAccesoRepository;
    
    @Autowired
    private TipoAccesoRepository tipoAccesoRepository;
    
    @Autowired
    private StatusUsuarioRepository statusUsuarioRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                      AuthenticationException exception) throws IOException, ServletException {
        
        String username = request.getParameter("username");
        String errorMessage = "Error de autenticación";
        String tipoAccesoNombre = "Bloqueado - Password incorrecto/Numero de intentos exedidos";
        
        if (exception instanceof UsernameNotFoundException) {
            errorMessage = "Usuario no encontrado";
            tipoAccesoNombre = "Usuario ingresado no existe";
        } else if (exception instanceof BadCredentialsException) {
            errorMessage = "Credenciales incorrectas";
            handleFailedLogin(username, request);
        } else if (exception instanceof LockedException) {
            errorMessage = "Cuenta bloqueada por múltiples intentos fallidos";
        } else if (exception instanceof DisabledException) {
            errorMessage = "Cuenta inactiva";
            tipoAccesoNombre = "Usuario Inactivo";
        }
        
        // Log failed access attempt
        logFailedAccess(username, tipoAccesoNombre, request);
        
        // Redirect with error message
        response.sendRedirect("/login?error=true&message=" + 
                             java.net.URLEncoder.encode(errorMessage, "UTF-8"));
    }
    
    private void handleFailedLogin(String username, HttpServletRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(username);
        
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            int intentosActuales = usuario.getIntentosDeAcceso() != null ? usuario.getIntentosDeAcceso() : 0;
            intentosActuales++;
            
            usuario.setIntentosDeAcceso(intentosActuales);
            
            // Check if user should be blocked based on company policy
            Integer maxIntentos = usuario.getSucursal().getEmpresa().getPasswordIntentosAntesDeBloquear();
            if (maxIntentos != null && intentosActuales >= maxIntentos) {
                // Block user
                StatusUsuario statusBloqueado = statusUsuarioRepository
                    .findByNombre("Bloqueado por intentos de acceso")
                    .orElse(null);
                
                if (statusBloqueado != null) {
                    usuario.setStatusUsuario(statusBloqueado);
                }
            }
            
            usuarioRepository.save(usuario);
        }
    }
    
    private void logFailedAccess(String username, String tipoAccesoNombre, HttpServletRequest request) {
        TipoAcceso tipoAcceso = tipoAccesoRepository.findByNombre(tipoAccesoNombre)
                .orElse(null);
        
        if (tipoAcceso != null) {
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setIdUsuario(username != null ? username : "UNKNOWN");
            bitacora.setTipoAcceso(tipoAcceso);
            bitacora.setFechaAcceso(LocalDateTime.now());
            bitacora.setDireccionIp(getClientIpAddress(request));
            bitacora.setHttpUserAgent(request.getHeader("User-Agent"));
            bitacora.setAccion("LOGIN_FAILED");
            
            // Parse User-Agent for browser and OS info
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                bitacora.setBrowser(extractBrowser(userAgent));
                bitacora.setSistemaOperativo(extractOS(userAgent));
                bitacora.setDispositivo(extractDevice(userAgent));
            }
            
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
    
    private String extractBrowser(String userAgent) {
        if (userAgent.contains("Chrome")) return "Chrome";
        if (userAgent.contains("Firefox")) return "Firefox";
        if (userAgent.contains("Safari")) return "Safari";
        if (userAgent.contains("Edge")) return "Edge";
        if (userAgent.contains("Opera")) return "Opera";
        return "Unknown";
    }
    
    private String extractOS(String userAgent) {
        if (userAgent.contains("Windows")) return "Windows";
        if (userAgent.contains("Mac OS")) return "macOS";
        if (userAgent.contains("Linux")) return "Linux";
        if (userAgent.contains("Android")) return "Android";
        if (userAgent.contains("iOS")) return "iOS";
        return "Unknown";
    }
    
    private String extractDevice(String userAgent) {
        if (userAgent.contains("Mobile")) return "Mobile";
        if (userAgent.contains("Tablet")) return "Tablet";
        return "Desktop";
    }
}
