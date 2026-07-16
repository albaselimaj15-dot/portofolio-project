package com.alba.portofolio.controller;

import com.alba.portofolio.dto.ProjectDto;
import com.alba.portofolio.entity.Project;
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
    public String projectDetails(@PathVariable Long id, Model model){


        ProjectDto project = projectService.getById(id);

        model.addAttribute("project", project);
        return "project-details";
    }
    @GetMapping("/portofolio")
    public String portofolio(Model model){
        model.addAttribute("projects",projectService.getAll());
        return "portofolio";
    }

    }
