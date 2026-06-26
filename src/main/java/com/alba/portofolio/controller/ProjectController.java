package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.dto.ProjectDto;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;


import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.CategoryRepository;

import com.alba.portofolio.repository.SkillRepository;
import com.alba.portofolio.repository.UserRepository;

import com.alba.portofolio.service.ProjectService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final CategoryRepository categoryRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public ProjectController(ProjectService projectService,
                             CategoryRepository categoryRepository,
                             SkillRepository skillRepository,
                             UserRepository userRepository,
                             ActivityService activityService) {
        this.projectService = projectService;
        this.categoryRepository = categoryRepository;
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    // LIST PAGE
    @GetMapping
    public String getProjects(@RequestParam(required = false) List<Long> skillIds,
                              @RequestParam(required = false) Long categoryId,
                              @RequestParam(required = false) String search,
                              Model model,
                              Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        List<Project> projects = projectService.filterPublic(search, categoryId,  skillIds);

        model.addAttribute("projects", projects);
        model.addAttribute("skills", skillRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);

        return "projects";
    }

    // CREATE (ONLY DTO FLOW)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createProject(@ModelAttribute ProjectDto dto,
                                @RequestParam Long categoryId,
                                @RequestParam(required = false) List<Long> skillIds,
                                @RequestParam("image") MultipartFile image,
                                Authentication auth) throws IOException {

        // mbrojtje nëse vjen null nga form
        if (skillIds == null) {
            skillIds = new ArrayList<>();
        }

        projectService.createProject(
                dto,
                categoryId,
                skillIds,
                image,
                auth.getName()
        );

        activityService.log("Project created by " + auth.getName());

        return "redirect:/projects";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String update(@ModelAttribute ProjectDto dto) {

        projectService.update(dto);

        activityService.log("Project updated");

        return "redirect:/projects";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {

        model.addAttribute("project", projectService.getById(id));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("skills",skillRepository.findAll());

        return "edit-project";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        projectService.delete(id);

        activityService.log("Project deleted");

        return "redirect:/projects";
    }

    @GetMapping("/portofolio")
    public String portfolio(Model model) {
        model.addAttribute("projects", projectService.getAll());
        return "portofolio";
    }

    @GetMapping("/{id}/categorize")
    public String categorizePage(@PathVariable Long id,Model model){
        model.addAttribute("project",projectService.getById(id));
        model.addAttribute("skills",skillRepository.findAll());
        model.addAttribute("categories",categoryRepository.findAll());
        return "categorize";
    }

    @PostMapping("/{id}/categorize")
    public  String saveCategorize(@PathVariable Long id,@RequestParam List<Long>skillId){
        projectService.assignSkills(id,skillId);
        return "redirect:/projects";
    }

}