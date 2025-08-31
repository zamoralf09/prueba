package com.umg.seguridadbravo.controller;

import com.umg.seguridadbravo.entity.*;
import com.umg.seguridadbravo.service.*;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin/permissions")
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ModuloService moduloService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private OpcionService opcionService;

    @GetMapping
    public String index(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Role> roles = roleService.findAll(pageable);

        model.addAttribute("roles", roles);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", roles.getTotalPages());

        return "admin/permissions/index";
    }

    @GetMapping("/role/{roleId}")
    public String manageRolePermissions(@PathVariable Integer roleId, Model model) {
        Optional<Role> role = roleService.findById(roleId);
        if (!role.isPresent()) {
            return "redirect:/admin/permissions?error=role_not_found";
        }

        List<Modulo> modulos = moduloService.findModulosWithMenus();
        List<RoleOpcion> currentPermissions = permissionService.findPermissionsByRole(role.get());

        model.addAttribute("role", role.get());
        model.addAttribute("modulos", modulos);
        model.addAttribute("currentPermissions", currentPermissions);

        return "admin/permissions/manage";
    }

    @PostMapping("/role/{roleId}/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateRolePermissions(
            @PathVariable Integer roleId,
            @RequestParam List<Long> opcionIds) {

        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Role> role = roleService.findById(roleId);
            if (!role.isPresent()) {
                response.put("success", false);
                response.put("message", "Role no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            String currentUser = SecurityUtils.getCurrentUsername();
            permissionService.updateRolePermissions(role.get(), opcionIds, currentUser);

            response.put("success", true);
            response.put("message", "Permisos actualizados correctamente");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al actualizar permisos: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/menu-structure")
    @ResponseBody
    public ResponseEntity<PermissionService.MenuStructure> getMenuStructure() {
        Usuario currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Corrected line
        }

        PermissionService.MenuStructure menuStructure = permissionService.buildMenuStructure(currentUser);
        return ResponseEntity.ok(menuStructure);
    }

    @GetMapping("/api/check/{opcionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Boolean>> checkPermission(@PathVariable Long opcionId) {
        Usuario currentUser = SecurityUtils.getCurrentUser();
        Map<String, Boolean> response = new HashMap<>();

        if (currentUser == null) {
            response.put("hasPermission", false);
            return ResponseEntity.ok(response);
        }

        boolean hasPermission = permissionService.hasPermission(currentUser.getRole(), opcionId);
        response.put("hasPermission", hasPermission);

        return ResponseEntity.ok(response);
    }

    // Gestión de Módulos
    @GetMapping("/modulos")
    public String modulosIndex(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Modulo> modulos = moduloService.findAll(pageable);

        model.addAttribute("modulos", modulos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", modulos.getTotalPages());

        return "admin/permissions/modulos";
    }

    @GetMapping("/modulos/new")
    public String newModulo(Model model) {
        model.addAttribute("modulo", new Modulo());
        return "admin/permissions/modulo-form";
    }

    @GetMapping("/modulos/{id}/edit")
    public String editModulo(@PathVariable Long id, Model model) {
        Optional<Modulo> modulo = moduloService.findById(id);
        if (!modulo.isPresent()) {
            return "redirect:/admin/permissions/modulos?error=not_found";
        }

        model.addAttribute("modulo", modulo.get());
        return "admin/permissions/modulo-form";
    }

    @PostMapping("/modulos")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveModulo(@RequestBody Modulo modulo) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validaciones
            if (modulo.getIdModulo() == null && moduloService.existsByNombre(modulo.getNombre())) {
                response.put("success", false);
                response.put("message", "Ya existe un módulo con ese nombre");
                return ResponseEntity.badRequest().body(response);
            }

            if (modulo.getIdModulo() != null && moduloService.existsByNombreAndIdNot(modulo.getNombre(), Long.valueOf(modulo.getIdModulo()))) {
                response.put("success", false);
                response.put("message", "Ya existe un módulo con ese nombre");
                return ResponseEntity.badRequest().body(response);
            }

            String currentUser = SecurityUtils.getCurrentUsername();
            if (modulo.getIdModulo() == null) {
                modulo.setUsuarioCreacion(currentUser);
            } else {
                modulo.setUsuarioModificacion(currentUser);
            }

            Modulo savedModulo = moduloService.save(modulo);
            response.put("success", true);
            response.put("message", "Módulo guardado correctamente");
            response.put("modulo", savedModulo);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al guardar módulo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/modulos/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteModulo(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            moduloService.deleteById(id.intValue());
            response.put("success", true);
            response.put("message", "Módulo eliminado correctamente");

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al eliminar módulo: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }

        return ResponseEntity.ok(response);
    }
}
