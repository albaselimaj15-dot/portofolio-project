package com.alba.portofolio.controller;

import com.alba.portofolio.entity.Category;
import com.alba.portofolio.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("/save")
    public String save(@ModelAttribute Category category) {

        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("category", categoryService.getById(id));
        model.addAttribute("categories", categoryService.getAll());

        return "categories";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirect) {

        try {
            categoryService.delete(id);
            redirect.addFlashAttribute("success", "Category deleted successfully");
        } catch (IllegalStateException e) {
            redirect.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/categories";
    }
}