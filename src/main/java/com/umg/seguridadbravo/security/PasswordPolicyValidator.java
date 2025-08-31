package com.umg.seguridadbravo.security;

import com.umg.seguridadbravo.entity.Empresa;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class PasswordPolicyValidator {

    public List<String> validatePassword(String password, Empresa empresa) {
        List<String> errors = new ArrayList<>();

        if (empresa == null) {
            errors.add("No se pudo obtener la configuración de políticas de contraseña");
            return errors;
        }

        // Check minimum length
        if (empresa.getPasswordLargo() != null && password.length() < empresa.getPasswordLargo()) {
            errors.add("La contraseña debe tener al menos " + empresa.getPasswordLargo() + " caracteres");
        }

        // Check uppercase letters
        if (empresa.getPasswordCantidadMayusculas() != null && empresa.getPasswordCantidadMayusculas() > 0) {
            long uppercaseCount = password.chars().filter(Character::isUpperCase).count();
            if (uppercaseCount < empresa.getPasswordCantidadMayusculas()) {
                errors.add("La contraseña debe contener al menos " + empresa.getPasswordCantidadMayusculas() + " letra(s) mayúscula(s)");
            }
        }

        // Check lowercase letters
        if (empresa.getPasswordCantidadMinusculas() != null && empresa.getPasswordCantidadMinusculas() > 0) {
            long lowercaseCount = password.chars().filter(Character::isLowerCase).count();
            if (lowercaseCount < empresa.getPasswordCantidadMinusculas()) {
                errors.add("La contraseña debe contener al menos " + empresa.getPasswordCantidadMinusculas() + " letra(s) minúscula(s)");
            }
        }

        // Check numbers
        if (empresa.getPasswordCantidadNumeros() != null && empresa.getPasswordCantidadNumeros() > 0) {
            long digitCount = password.chars().filter(Character::isDigit).count();
            if (digitCount < empresa.getPasswordCantidadNumeros()) {
                errors.add("La contraseña debe contener al menos " + empresa.getPasswordCantidadNumeros() + " número(s)");
            }
        }

        // Check special characters
        if (empresa.getPasswordCantidadCaracteresEspeciales() != null && empresa.getPasswordCantidadCaracteresEspeciales() > 0) {
            Pattern specialCharPattern = Pattern.compile("[^a-zA-Z0-9]");
            long specialCharCount = password.chars()
                    .filter(ch -> specialCharPattern.matcher(String.valueOf((char) ch)).matches())
                    .count();
            if (specialCharCount < empresa.getPasswordCantidadCaracteresEspeciales()) {
                errors.add("La contraseña debe contener al menos " + empresa.getPasswordCantidadCaracteresEspeciales() + " caracter(es) especial(es)");
            }
        }

        return errors;
    }

    public String generatePasswordRequirements(Empresa empresa) {
        if (empresa == null) {
            return "No se pudieron cargar los requisitos de contraseña";
        }

        StringBuilder requirements = new StringBuilder("La contraseña debe cumplir con los siguientes requisitos:\n");

        if (empresa.getPasswordLargo() != null) {
            requirements.append("• Mínimo ").append(empresa.getPasswordLargo()).append(" caracteres\n");
        }

        if (empresa.getPasswordCantidadMayusculas() != null && empresa.getPasswordCantidadMayusculas() > 0) {
            requirements.append("• Al menos ").append(empresa.getPasswordCantidadMayusculas()).append(" letra(s) mayúscula(s)\n");
        }

        if (empresa.getPasswordCantidadMinusculas() != null && empresa.getPasswordCantidadMinusculas() > 0) {
            requirements.append("• Al menos ").append(empresa.getPasswordCantidadMinusculas()).append(" letra(s) minúscula(s)\n");
        }

        if (empresa.getPasswordCantidadNumeros() != null && empresa.getPasswordCantidadNumeros() > 0) {
            requirements.append("• Al menos ").append(empresa.getPasswordCantidadNumeros()).append(" número(s)\n");
        }

        if (empresa.getPasswordCantidadCaracteresEspeciales() != null && empresa.getPasswordCantidadCaracteresEspeciales() > 0) {
            requirements.append("• Al menos ").append(empresa.getPasswordCantidadCaracteresEspeciales()).append(" caracter(es) especial(es)\n");
        }

        if (empresa.getPasswordCantidadCaducidadDias() != null) {
            requirements.append("• La contraseña expira cada ").append(empresa.getPasswordCantidadCaducidadDias()).append(" días\n");
        }

        return requirements.toString();
    }
}
