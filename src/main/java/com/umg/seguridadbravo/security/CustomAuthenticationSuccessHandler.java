package com.umg.seguridadbravo.security;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.entity.BitacoraAcceso;
import com.umg.seguridadbravo.entity.TipoAcceso;
import com.umg.seguridadbravo.repository.UsuarioRepository;
import com.umg.seguridadbravo.repository.BitacoraAccesoRepository;
import com.umg.seguridadbravo.repository.TipoAccesoRepository;
import com.umg.seguridadbravo.security.CustomUserDetailsService;
import com.umg.seguridadbravo.security.CustomUserDetailsService.CustomUserPrincipal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BitacoraAccesoRepository bitacoraAccesoRepository;
    
    @Autowired
    private TipoAccesoRepository tipoAccesoRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        Usuario usuario = userPrincipal.getUsuario();
        
        // Generate session ID
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        
        // Update user login information
        usuario.setUltimaFechaIngreso(LocalDateTime.now());
        usuario.setIntentosDeAcceso(0);
        usuario.setSesionActual(sessionId);
        usuarioRepository.save(usuario);
        
        // Log successful access
        TipoAcceso tipoAccesoExitoso = tipoAccesoRepository.findByNombre("Acceso Concedido")
                .orElse(null);
        
        if (tipoAccesoExitoso != null) {
            BitacoraAcceso bitacora = new BitacoraAcceso();
            bitacora.setIdUsuario(usuario.getIdUsuario());
            bitacora.setTipoAcceso(tipoAccesoExitoso);
            bitacora.setFechaAcceso(LocalDateTime.now());
            bitacora.setDireccionIp(getClientIpAddress(request));
            bitacora.setHttpUserAgent(request.getHeader("User-Agent"));
            bitacora.setAccion("LOGIN_SUCCESS");
            bitacora.setSesion(sessionId);
            
            // Parse User-Agent for browser and OS info
            String userAgent = request.getHeader("User-Agent");
            if (userAgent != null) {
                bitacora.setBrowser(extractBrowser(userAgent));
                bitacora.setSistemaOperativo(extractOS(userAgent));
                bitacora.setDispositivo(extractDevice(userAgent));
            }
            
            bitacoraAccesoRepository.save(bitacora);
        }
        
        // Store user info in session
        session.setAttribute("currentUser", usuario);
        session.setAttribute("userFullName", usuario.getNombreCompleto());
        session.setAttribute("userRole", usuario.getRole().getNombre());
        
        // Redirect based on role or requested page
        String targetUrl = determineTargetUrl(request, usuario);
        response.sendRedirect(targetUrl);
    }
    
    private String determineTargetUrl(HttpServletRequest request, Usuario usuario) {
        // Check if there was a saved request
        String savedRequest = (String) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        if (savedRequest != null) {
            request.getSession().removeAttribute("SPRING_SECURITY_SAVED_REQUEST");
            return savedRequest;
        }
        
        // Check if password needs to be changed
        if (usuario.getRequiereCambiarPassword() != null && usuario.getRequiereCambiarPassword() == 1) {
            return "/usuario/change-password?required=true";
        }
        
        // Default redirect to dashboard
        return "/dashboard";
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
