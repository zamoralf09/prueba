package com.umg.seguridadbravo.controller;

import com.umg.seguridadbravo.entity.Empresa;
import com.umg.seguridadbravo.service.EmpresaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/empresa")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class EmpresaController {

    @Autowired
    private EmpresaService empresaService;

    @GetMapping
    public String listEmpresas(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "nombre") String sortBy,
                              @RequestParam(defaultValue = "asc") String sortDir,
                              @RequestParam(required = false) String search,
                              Model model) {
        
        if (search != null && !search.trim().isEmpty()) {
            List<Empresa> searchResults = empresaService.findByNombreContaining(search);
            model.addAttribute("empresas", searchResults);
            model.addAttribute("search", search);
        } else {
            Page<Empresa> empresas = empresaService.findAllPageable(page, size, sortBy, sortDir);
            model.addAttribute("empresas", empresas);
        }
        
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        
        return "empresa/list";
    }

    @GetMapping("/create")
    public String createEmpresaForm(Model model) {
        model.addAttribute("empresa", new Empresa());
        return "empresa/form";
    }

    @PostMapping("/create")
    public String createEmpresa(@Valid @ModelAttribute Empresa empresa,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "empresa/form";
        }
        
        if (empresaService.existsByNit(empresa.getNit())) {
            result.rejectValue("nit", "error.nit", "Ya existe una empresa con este NIT");
            return "empresa/form";
        }
        
        try {
            empresaService.save(empresa);
            redirectAttributes.addFlashAttribute("success", "Empresa creada exitosamente");
            return "redirect:/empresa";
        } catch (Exception e) {
            result.reject("error.general", e.getMessage());
            return "empresa/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editEmpresaForm(@PathVariable Integer id, Model model) {
        Optional<Empresa> empresaOpt = empresaService.findById(id);
        
        if (empresaOpt.isEmpty()) {
            return "redirect:/empresa?error=Empresa no encontrada";
        }
        
        model.addAttribute("empresa", empresaOpt.get());
        model.addAttribute("editing", true);
        return "empresa/form";
    }

    @PostMapping("/edit/{id}")
    public String editEmpresa(@PathVariable Integer id,
                             @Valid @ModelAttribute Empresa empresa,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "empresa/form";
        }
        
        try {
            empresa.setIdEmpresa(id);
            empresaService.save(empresa);
            redirectAttributes.addFlashAttribute("success", "Empresa actualizada exitosamente");
            return "redirect:/empresa";
        } catch (Exception e) {
            result.reject("error.general", e.getMessage());
            return "empresa/form";
        }
    }

    @GetMapping("/view/{id}")
    public String viewEmpresa(@PathVariable Integer id, Model model) {
        Optional<Empresa> empresaOpt = empresaService.findById(id);
        
        if (empresaOpt.isEmpty()) {
            return "redirect:/empresa?error=Empresa no encontrada";
        }
        
        model.addAttribute("empresa", empresaOpt.get());
        return "empresa/view";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmpresa(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            empresaService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Empresa eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar empresa: " + e.getMessage());
        }
        
        return "redirect:/empresa";
    }
}
