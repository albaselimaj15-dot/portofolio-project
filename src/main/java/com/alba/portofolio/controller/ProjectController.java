package com.alba.portofolio.controller;

import com.alba.portofolio.entity.Project;
import com.alba.portofolio.entity.User;


import com.alba.portofolio.repository.ProjectRepository;
import com.alba.portofolio.repository.UserRepository;

import com.alba.portofolio.service.ProjectService;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, ProjectService projectService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectService = projectService;
    }

    @GetMapping
    public String getProjects(Model model, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();


        model.addAttribute("projects", projectRepository.findAllByUser(user));

        return "projects";
    }
    @PostMapping
    public String create(@ModelAttribute Project project, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        projectService.add(project);

        return "redirect:/projects";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        Project project = projectRepository.findById(id)
                .orElseThrow();

        model.addAttribute("project", project);

        return "edit-project";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute Project project) {

        projectService.update(project.getId(), project);

        return "redirect:/projects";
    }
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        projectRepository.deleteById(id);

        return "redirect:/projects";
    }
}