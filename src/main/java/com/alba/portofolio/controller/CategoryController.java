package com.alba.portofolio.controller;

import com.alba.portofolio.entity.Category;
import com.alba.portofolio.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("category", new Category());

        return "categories";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("category") Category category, BindingResult result,
                       Model model,RedirectAttributes redirectAttributes) {

        if(result.hasErrors()){
            model.addAttribute("categories",categoryService.getAll());
            return "categories";
        }

        categoryService.save(category);
        if(category.getId() == null) {
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Category created successfully!"
            );
        } else {
            redirectAttributes.addFlashAttribute(
                    "success",
                    "Category updated successfully!"
            );
        }
        return "redirect:/categories";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("category", categoryService.getById(id));
        model.addAttribute("categories", categoryService.getAll());

        return "categories";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {

        try {
            categoryService.delete(id);
            redirect.addFlashAttribute("success", "Category deleted successfully!");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }


        return "redirect:/categories";
    }
}