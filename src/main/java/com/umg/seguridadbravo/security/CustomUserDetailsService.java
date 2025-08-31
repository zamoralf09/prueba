package com.umg.seguridadbravo.security;

import com.umg.seguridadbravo.entity.Usuario;
import com.umg.seguridadbravo.entity.RoleOpcion;
import com.umg.seguridadbravo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByIdUsuarioWithPermissions(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new CustomUserPrincipal(usuario);
    }

    public static class CustomUserPrincipal implements UserDetails {
        private Usuario usuario;

        public CustomUserPrincipal(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();
            
            // Add role authority
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getRole().getNombre().toUpperCase()));
            
            // Add specific permissions based on RoleOpcion
            if (usuario.getRole().getRoleOpciones() != null) {
                for (RoleOpcion roleOpcion : usuario.getRole().getRoleOpciones()) {
                    String opcionNombre = roleOpcion.getOpcion().getNombre().toUpperCase().replace(" ", "_");
                    
                    if (roleOpcion.getAlta()) {
                        authorities.add(new SimpleGrantedAuthority("PERM_" + opcionNombre + "_CREATE"));
                    }
                    if (roleOpcion.getBaja()) {
                        authorities.add(new SimpleGrantedAuthority("PERM_" + opcionNombre + "_DELETE"));
                    }
                    if (roleOpcion.getCambio()) {
                        authorities.add(new SimpleGrantedAuthority("PERM_" + opcionNombre + "_UPDATE"));
                    }
                    if (roleOpcion.getImprimir()) {
                        authorities.add(new SimpleGrantedAuthority("PERM_" + opcionNombre + "_PRINT"));
                    }
                    if (roleOpcion.getExportar()) {
                        authorities.add(new SimpleGrantedAuthority("PERM_" + opcionNombre + "_EXPORT"));
                    }
                }
            }
            
            return authorities;
        }

        @Override
        public String getPassword() {
            return usuario.getPassword();
        }

        @Override
        public String getUsername() {
            return usuario.getIdUsuario();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return !usuario.isBloqueado();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            // Check if password needs to be changed based on company policy
            if (usuario.getRequiereCambiarPassword() != null && usuario.getRequiereCambiarPassword() == 1) {
                return false;
            }
            
            // Check password expiration based on company policy
            if (usuario.getUltimaFechaCambioPassword() != null && 
                usuario.getSucursal().getEmpresa().getPasswordCantidadCaducidadDias() != null) {
                
                long daysSinceLastChange = java.time.temporal.ChronoUnit.DAYS.between(
                    usuario.getUltimaFechaCambioPassword().toLocalDate(),
                    java.time.LocalDate.now()
                );
                
                return daysSinceLastChange < usuario.getSucursal().getEmpresa().getPasswordCantidadCaducidadDias();
            }
            
            return true;
        }

        @Override
        public boolean isEnabled() {
            return usuario.isActivo();
        }

        public Usuario getUsuario() {
            return usuario;
        }
    }
}
