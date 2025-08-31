package com.umg.seguridadbravo.controller;

import com.umg.seguridadbravo.service.*;
import com.umg.seguridadbravo.entity.BitacoraAcceso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private AuditService auditService;

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Estadísticas generales
        model.addAttribute("totalUsuarios", usuarioService.countAll());
        model.addAttribute("usuariosActivos", usuarioService.countByActivoTrue());
        model.addAttribute("totalRoles", roleService.countAll());
        model.addAttribute("sesionesActivas", sessionService.countActiveSessions());
        
        // Actividad reciente (últimas 24 horas)
        LocalDateTime desde = LocalDateTime.now().minusDays(1);
        List<BitacoraAcceso> actividadReciente = auditService.findRecentActivity(desde, 10);
        model.addAttribute("actividadReciente", actividadReciente);
        
        // Alertas del sistema
        List<String> alertas = auditService.getSystemAlerts();
        model.addAttribute("alertas", alertas);
        
        model.addAttribute("pageTitle", "Dashboard");
        
        return "dashboard";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        model.addAttribute("pageTitle", "Acceso Denegado");
        return "error/access-denied";
    }
}
