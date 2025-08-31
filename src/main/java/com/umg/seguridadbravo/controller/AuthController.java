package com.umg.seguridadbravo.controller;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.service.AuthenticationService;
import com.umg.seguridadbravo.service.SessionService;
import com.umg.seguridadbravo.security.SecurityUtils;
import com.umg.seguridadbravo.security.PasswordPolicyValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AuthController {

    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private SessionService sessionService;
    
    @Autowired
    private PasswordPolicyValidator passwordPolicyValidator;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           @RequestParam(value = "expired", required = false) String expired,
                           @RequestParam(value = "message", required = false) String message,
                           Model model) {
        
        if (error != null) {
            model.addAttribute("error", true);
            model.addAttribute("errorMessage", message != null ? message : "Credenciales incorrectas");
        }
        
        if (logout != null) {
            model.addAttribute("logout", true);
            model.addAttribute("logoutMessage", "Ha cerrado sesión exitosamente");
        }
        
        if (expired != null) {
            model.addAttribute("expired", true);
            model.addAttribute("expiredMessage", "Su sesión ha expirado");
        }
        
        return "login";
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String username,
                                      RedirectAttributes redirectAttributes) {
        try {
            String resetToken = authenticationService.generatePasswordResetToken(username);
            
            if (resetToken != null) {
                redirectAttributes.addFlashAttribute("success", 
                    "Se han enviado las instrucciones de recuperación a su correo electrónico");
            } else {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado");
            }
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la solicitud");
        }
        
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam(required = false) String token,
                                   Model model) {
        if (token == null) {
            return "redirect:/login?error=true&message=Token de recuperación inválido";
        }
        
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                     @RequestParam String username,
                                     @RequestParam String securityAnswer,
                                     @RequestParam String newPassword,
                                     @RequestParam String confirmPassword,
                                     RedirectAttributes redirectAttributes) {
        
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/reset-password?token=" + token;
        }
        
        try {
            boolean success = authenticationService.resetPassword(username, securityAnswer, newPassword);
            
            if (success) {
                redirectAttributes.addFlashAttribute("success", 
                    "Contraseña restablecida exitosamente. Puede iniciar sesión con su nueva contraseña");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", 
                    "Respuesta de seguridad incorrecta o usuario no válido");
                return "redirect:/reset-password?token=" + token;
            }
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reset-password?token=" + token;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al restablecer la contraseña");
            return "redirect:/reset-password?token=" + token;
        }
    }

    @GetMapping("/change-password")
    public String changePasswordPage(@RequestParam(value = "required", required = false) String required,
                                   Model model) {
        Usuario currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        if (required != null) {
            model.addAttribute("required", true);
            model.addAttribute("message", "Debe cambiar su contraseña para continuar");
        }
        
        // Add password policy requirements
        String requirements = passwordPolicyValidator.generatePasswordRequirements(
            currentUser.getSucursal().getEmpresa()
        );
        model.addAttribute("passwordRequirements", requirements);
        
        return "change-password";
    }

    @PostMapping("/change-password")
    public String processChangePassword(@RequestParam String currentPassword,
                                      @RequestParam String newPassword,
                                      @RequestParam String confirmPassword,
                                      RedirectAttributes redirectAttributes,
                                      HttpServletRequest request) {
        
        Usuario currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/change-password";
        }
        
        try {
            boolean success = authenticationService.changePassword(
                currentUser.getIdUsuario(), currentPassword, newPassword
            );
            
            if (success) {
                authenticationService.logUserActivity(
                    currentUser.getIdUsuario(), "PASSWORD_CHANGED", request
                );
                
                redirectAttributes.addFlashAttribute("success", "Contraseña cambiada exitosamente");
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("error", "Contraseña actual incorrecta");
                return "redirect:/change-password";
            }
            
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/change-password";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar la contraseña");
            return "redirect:/change-password";
        }
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpSession session) {
        Usuario currentUser = SecurityUtils.getCurrentUser();
        if (currentUser != null) {
            authenticationService.logout(currentUser.getIdUsuario(), request);
            sessionService.invalidateSession(session.getId());
        }
        
        return "redirect:/login?logout=true";
    }

    @GetMapping("/access-denied")
    public String accessDeniedPage() {
        return "access-denied";
    }
}
