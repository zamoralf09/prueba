package com.umg.seguridadbravo.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    public boolean sendPasswordResetEmail(String email, String username, String resetToken) {
        try {
            
            String subject = "Recuperación de Contraseña - Sistema de Seguridad";
            String body = buildPasswordResetEmailBody(username, resetToken);
            
            logger.info("Sending password reset email to: {}", email);
            logger.info("Email subject: {}", subject);
            logger.info("Email body: {}", body);
            
            // Simulate email sending
            return true;
            
        } catch (Exception e) {
            logger.error("Error sending password reset email to: {}", email, e);
            return false;
        }
    }

    public boolean sendAccountLockedEmail(String email, String username) {
        try {
            String subject = "Cuenta Bloqueada - Sistema de Seguridad";
            String body = buildAccountLockedEmailBody(username);
            
            logger.info("Sending account locked email to: {}", email);
            logger.info("Email subject: {}", subject);
            logger.info("Email body: {}", body);
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error sending account locked email to: {}", email, e);
            return false;
        }
    }

    public boolean sendPasswordExpirationWarning(String email, String username, int daysUntilExpiration) {
        try {
            String subject = "Advertencia: Su contraseña expirará pronto";
            String body = buildPasswordExpirationWarningBody(username, daysUntilExpiration);
            
            logger.info("Sending password expiration warning to: {}", email);
            logger.info("Email subject: {}", subject);
            logger.info("Email body: {}", body);
            
            return true;
            
        } catch (Exception e) {
            logger.error("Error sending password expiration warning to: {}", email, e);
            return false;
        }
    }

    private String buildPasswordResetEmailBody(String username, String resetToken) {
        return String.format("""
            Estimado/a %s,
            
            Hemos recibido una solicitud para restablecer la contraseña de su cuenta en el Sistema de Seguridad.
            
            Para restablecer su contraseña, haga clic en el siguiente enlace:
            http://localhost:8080/security-system/reset-password?token=%s
            
            Si no solicitó este restablecimiento, ignore este correo electrónico.
            
            Por seguridad, este enlace expirará en 24 horas.
            
            Atentamente,
            Equipo de Seguridad del Sistema
            """, username, resetToken);
    }

    private String buildAccountLockedEmailBody(String username) {
        return String.format("""
            Estimado/a %s,
            
            Su cuenta en el Sistema de Seguridad ha sido bloqueada debido a múltiples intentos fallidos de inicio de sesión.
            
            Por favor, contacte al administrador del sistema para desbloquear su cuenta.
            
            Si no fue usted quien intentó acceder a la cuenta, le recomendamos cambiar su contraseña una vez que su cuenta sea desbloqueada.
            
            Atentamente,
            Equipo de Seguridad del Sistema
            """, username);
    }

    private String buildPasswordExpirationWarningBody(String username, int daysUntilExpiration) {
        return String.format("""
            Estimado/a %s,
            
            Su contraseña en el Sistema de Seguridad expirará en %d día(s).
            
            Para evitar interrupciones en el acceso a su cuenta, le recomendamos cambiar su contraseña antes de la fecha de expiración.
            
            Puede cambiar su contraseña iniciando sesión en el sistema y accediendo a la opción "Cambiar Contraseña" en su perfil.
            
            Atentamente,
            Equipo de Seguridad del Sistema
            """, username, daysUntilExpiration);
    }
}
