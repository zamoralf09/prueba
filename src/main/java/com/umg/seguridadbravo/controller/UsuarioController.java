package com.umg.seguridadbravo.controller;

import com.umg.seguridadbravo.entity.*;
import com.umg.seguridadbravo.service.*;
import com.umg.seguridadbravo.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EmpresaService empresaService;
    
    @Autowired
    private SucursalService sucursalService;
    
    @Autowired
    private GeneroService generoService;
    
    @Autowired
    private StatusUsuarioService statusUsuarioService;
    
    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_READ')")
    public String listUsuarios(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "idUsuario") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDir,
                              @RequestParam(required = false) String search,
                              Model model) {
        
        Page<Usuario> usuarios;
        
        if (search != null && !search.trim().isEmpty()) {
            List<Usuario> searchResults = usuarioService.findByNombreOrApellido(search);
            model.addAttribute("usuarios", searchResults);
            model.addAttribute("search", search);
        } else {
            usuarios = usuarioService.findAllPageable(page, size, sortBy, sortDir);
            model.addAttribute("usuarios", usuarios);
        }
        
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "usuario/list";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_CREATE')")
    public String createUsuarioForm(Model model) {
        model.addAttribute("usuario", new Usuario());
        loadFormData(model);
        return "usuario/form";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_CREATE')")
    public String createUsuario(@Valid @ModelAttribute Usuario usuario,
                               BindingResult result,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               @RequestParam(required = false) MultipartFile fotografiaFile,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            loadFormData(model);
            return "usuario/form";
        }
        
        if (!password.equals(confirmPassword)) {
            result.rejectValue("password", "error.password", "Las contraseñas no coinciden");
            loadFormData(model);
            return "usuario/form";
        }
        
        if (usuarioService.existsById(usuario.getIdUsuario())) {
            result.rejectValue("idUsuario", "error.idUsuario", "El usuario ya existe");
            loadFormData(model);
            return "usuario/form";
        }
        
        try {
            usuarioService.create(usuario, password);
            
            if (fotografiaFile != null && !fotografiaFile.isEmpty()) {
                usuarioService.updateUserPhoto(usuario.getIdUsuario(), fotografiaFile);
            }
            
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente");
            return "redirect:/usuario";
            
        } catch (Exception e) {
            result.rejectValue("password", "error.password", e.getMessage());
            loadFormData(model);
            return "usuario/form";
        }
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_UPDATE')")
    public String editUsuarioForm(@PathVariable String id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.findById(id);
        
        if (usuarioOpt.isEmpty()) {
            return "redirect:/usuario?error=Usuario no encontrado";
        }
        
        model.addAttribute("usuario", usuarioOpt.get());
        model.addAttribute("editing", true);
        loadFormData(model);
        return "usuario/form";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_UPDATE')")
    public String editUsuario(@PathVariable String id,
                             @Valid @ModelAttribute Usuario usuario,
                             BindingResult result,
                             @RequestParam(required = false) MultipartFile fotografiaFile,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("editing", true);
            loadFormData(model);
            return "usuario/form";
        }
        
        try {
            usuario.setIdUsuario(id); // Ensure ID is set
            usuarioService.update(usuario);
            
            if (fotografiaFile != null && !fotografiaFile.isEmpty()) {
                usuarioService.updateUserPhoto(id, fotografiaFile);
            }
            
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente");
            return "redirect:/usuario";
            
        } catch (Exception e) {
            result.reject("error.general", e.getMessage());
            model.addAttribute("editing", true);
            loadFormData(model);
            return "usuario/form";
        }
    }

    @GetMapping("/view/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_READ')")
    public String viewUsuario(@PathVariable String id, Model model) {
        Optional<Usuario> usuarioOpt = usuarioService.findByIdWithPermissions(id);
        
        if (usuarioOpt.isEmpty()) {
            return "redirect:/usuario?error=Usuario no encontrado";
        }
        
        model.addAttribute("usuario", usuarioOpt.get());
        return "usuario/view";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or hasAuthority('PERM_USUARIOS_DELETE')")
    public String deleteUsuario(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Usuario desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar usuario: " + e.getMessage());
        }
        
        return "redirect:/usuario";
    }

    @PostMapping("/unlock/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String unlockUsuario(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            boolean success = usuarioService.unlockUser(id);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Usuario desbloqueado exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo desbloquear el usuario");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desbloquear usuario: " + e.getMessage());
        }
        
        return "redirect:/usuario";
    }

    @PostMapping("/change-password/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String changeUserPassword(@PathVariable String id,
                                   @RequestParam String newPassword,
                                   @RequestParam String confirmPassword,
                                   RedirectAttributes redirectAttributes) {
        
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden");
            return "redirect:/usuario/view/" + id;
        }
        
        try {
            boolean success = usuarioService.changePassword(id, newPassword);
            if (success) {
                redirectAttributes.addFlashAttribute("success", "Contraseña cambiada exitosamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo cambiar la contraseña");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar contraseña: " + e.getMessage());
        }
        
        return "redirect:/usuario/view/" + id;
    }

    @GetMapping("/photo/{id}")
    public ResponseEntity<byte[]> getUserPhoto(@PathVariable String id) {
        byte[] photo = usuarioService.getUserPhoto(id);
        
        if (photo == null) {
            return ResponseEntity.notFound().build();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(photo.length);
        
        return new ResponseEntity<>(photo, headers, HttpStatus.OK);
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        Usuario currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("usuario", currentUser);
        return "usuario/profile";
    }

    @PostMapping("/profile")
    public String updateProfile(@Valid @ModelAttribute Usuario usuario,
                               BindingResult result,
                               @RequestParam(required = false) MultipartFile fotografiaFile,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        
        Usuario currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            return "redirect:/login";
        }
        
        if (result.hasErrors()) {
            return "usuario/profile";
        }
        
        try {
            // Only allow updating certain fields in profile
            currentUser.setNombre(usuario.getNombre());
            currentUser.setApellido(usuario.getApellido());
            currentUser.setCorreoElectronico(usuario.getCorreoElectronico());
            currentUser.setTelefonoMovil(usuario.getTelefonoMovil());
            
            usuarioService.update(currentUser);
            
            if (fotografiaFile != null && !fotografiaFile.isEmpty()) {
                usuarioService.updateUserPhoto(currentUser.getIdUsuario(), fotografiaFile);
            }
            
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
            return "redirect:/usuario/profile";
            
        } catch (Exception e) {
            result.reject("error.general", e.getMessage());
            return "usuario/profile";
        }
    }

    private void loadFormData(Model model) {
        model.addAttribute("empresas", empresaService.findAll());
        model.addAttribute("sucursales", sucursalService.findAll());
        model.addAttribute("generos", generoService.findAll());
        model.addAttribute("statusUsuarios", statusUsuarioService.findAll());
        model.addAttribute("roles", roleService.findAll());
    }
}
