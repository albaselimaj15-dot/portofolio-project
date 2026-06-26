package com.alba.portofolio.controller;

import com.alba.portofolio.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PortofolioPreview {

    private final ProjectService projectService;

    public PortofolioPreview(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/portofolio/project/{id}")
    public String portofolio(@PathVariable Long id, Model model){
        model.addAttribute("projects",projectService.getById(id));
        return "projects";
    }
    @GetMapping("/portofolio")
    public String portofolio(Model model){
        model.addAttribute("projects",projectService.getAll());
        return "portofolio";
    }

    }
