package com.umg.seguridadbravo.service;

import com.umg.seguridadbravo.entity.*;
import com.umg.seguridadbravo.repository.RoleOpcionRepository;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class PermissionService {

    @Autowired
    private RoleOpcionRepository roleOpcionRepository;

    @Autowired
    private OpcionService opcionService;

    @Autowired
    private ModuloService moduloService;

    @Autowired
    private MenuService menuService;

    public List<RoleOpcion> findPermissionsByRole(Role role) {
        return roleOpcionRepository.findByRoleAndActivoTrue(role);
    }

    public boolean hasPermission(Role role, Long opcionId) {
        return roleOpcionRepository.existsByRoleAndOpcionIdAndActivoTrue(role, opcionId);
    }

    public boolean hasPermission(Usuario usuario, String opcionNombre) {
        return roleOpcionRepository.existsByRoleAndOpcionNombreAndActivoTrue(usuario.getRole(), opcionNombre);
    }

    public void assignPermission(Role role, Opcion opcion, String usuarioCreacion) {
        if (!hasPermission(role, Long.valueOf(opcion.getIdOpcion()))) {
            RoleOpcion roleOpcion = new RoleOpcion();
            roleOpcion.setRole(role);
            roleOpcion.setOpcion(opcion);
            roleOpcion.setFechaCreacion(LocalDateTime.now());
            roleOpcion.setUsuarioCreacion(usuarioCreacion);
            roleOpcionRepository.save(roleOpcion);
        }
    }

    public void revokePermission(Role role, Opcion opcion, String usuarioModificacion) {
        Optional<RoleOpcion> roleOpcion = roleOpcionRepository.findByRoleAndOpcionAndActivoTrue(role, opcion);
        if (roleOpcion.isPresent()) {
            RoleOpcion ro = roleOpcion.get();
            ro.setFechaModificacion(LocalDateTime.now());
            ro.setUsuarioModificacion(usuarioModificacion);
            roleOpcionRepository.save(ro);
        }
    }

    public void updateRolePermissions(Role role, List<Long> opcionIds, String usuario) {
        // Desactivar todos los permisos actuales
        List<RoleOpcion> currentPermissions = roleOpcionRepository.findByRoleAndActivoTrue(role);
        for (RoleOpcion ro : currentPermissions) {
            ro.setFechaModificacion(LocalDateTime.now());
            ro.setUsuarioModificacion(usuario);
            roleOpcionRepository.save(ro);
        }

        // Asignar nuevos permisos
        for (Long opcionId : opcionIds) {
            Optional<Opcion> opcion = opcionService.findById(opcionId);
            if (opcion.isPresent()) {
                assignPermission(role, opcion.get(), usuario);
            }
        }
    }

    public MenuStructure buildMenuStructure(Usuario usuario) {
        List<Opcion> userOpciones = opcionService.findOpcionesByRoleId(usuario.getRole().getIdRole());
        
        MenuStructure menuStructure = new MenuStructure();
        
        // Agrupar opciones por módulo y menú
        Set<Long> moduloIds = userOpciones.stream()
                .map(o -> Long.valueOf(o.getMenu().getModulo().getIdModulo()))
                .collect(Collectors.toSet());
        
        for (Long moduloId : moduloIds) {
            Optional<Modulo> modulo = moduloService.findById(moduloId);
            if (modulo.isPresent()) {
                ModuloMenu moduloMenu = new ModuloMenu();
                moduloMenu.setModulo(modulo.get());
                
                Set<Long> menuIds = userOpciones.stream()
                        .filter(o -> o.getMenu().getModulo().getIdModulo().equals(moduloId))
                        .map(o -> Long.valueOf(o.getMenu().getIdMenu()))
                        .collect(Collectors.toSet());
                
                for (Long menuId : menuIds) {
                    Optional<Menu> menu = menuService.findById(menuId);
                    if (menu.isPresent()) {
                        MenuOpciones menuOpciones = new MenuOpciones();
                        menuOpciones.setMenu(menu.get());
                        
                        List<Opcion> opciones = userOpciones.stream()
                                .filter(o -> o.getMenu().getIdMenu().equals(menuId))
                                .collect(Collectors.toList());
                        
                        menuOpciones.setOpciones(opciones);
                        moduloMenu.getMenus().add(menuOpciones);
                    }
                }
                
                menuStructure.getModulos().add(moduloMenu);
            }
        }
        
        return menuStructure;
    }
    // Clases auxiliares para estructura de menú
    public static class MenuStructure {
        private List<ModuloMenu> modulos = new java.util.ArrayList<>();
        
        public List<ModuloMenu> getModulos() { return modulos; }
        public void setModulos(List<ModuloMenu> modulos) { this.modulos = modulos; }
    }
    
    public static class ModuloMenu {
        private Modulo modulo;
        private List<MenuOpciones> menus = new java.util.ArrayList<>();
        
        public Modulo getModulo() { return modulo; }
        public void setModulo(Modulo modulo) { this.modulo = modulo; }
        public List<MenuOpciones> getMenus() { return menus; }
        public void setMenus(List<MenuOpciones> menus) { this.menus = menus; }
    }
    
    public static class MenuOpciones {
        private Menu menu;
        private List<Opcion> opciones;
        
        public Menu getMenu() { return menu; }
        public void setMenu(Menu menu) { this.menu = menu; }
        public List<Opcion> getOpciones() { return opciones; }
        public void setOpciones(List<Opcion> opciones) { this.opciones = opciones; }
    }
}
