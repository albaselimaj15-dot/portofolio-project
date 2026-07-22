package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.dto.ProjectDto;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;


import com.alba.portofolio.entity.Skill;
import com.alba.portofolio.enums.Role;

import com.alba.portofolio.repository.UserRepository;

import com.alba.portofolio.service.CategoryService;
import com.alba.portofolio.service.ProjectService;
import com.alba.portofolio.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final CategoryService categoryService;
    private final SkillService skillService;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public ProjectController(ProjectService projectService,
                              CategoryService categoryService, SkillService skillService,
                             UserRepository userRepository,
                             ActivityService activityService) {
        this.projectService = projectService;
        this.categoryService = categoryService;
        ;
        this.skillService = skillService;

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
        model.addAttribute("skills",skillService.getAll() );
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);

        return "projects";
    }

    // CREATE (ONLY DTO FLOW)
    @PostMapping
    public String createProject(
            @Valid @ModelAttribute("project") ProjectDto dto,
            BindingResult result,
            @RequestParam(required = false) List<Long> skillIds,
            @RequestParam(value = "image", required = false) MultipartFile image,
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {


        if (result.hasErrors()) {

            model.addAttribute("categories", categoryService.getAll());
            model.addAttribute("skills", skillService.getAll());

            return "projects/create";
        }


        String email = authentication.getName();

        try {
            projectService.createProject(
                    dto,
                    dto.getCategoryId(),
                    skillIds,
                    image,
                    email
            );

            activityService.log("Project created");

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Project created successfully"
            );
        } catch (Exception e) {


            e.printStackTrace();
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Something went wrong."
            );

        }

        return "redirect:/projects";
    }
    @GetMapping("/create")
    public String createProjectForm(Model model){

        model.addAttribute("project", new ProjectDto());
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("skills", skillService.getAll());

        return "projects/create";
    }




    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute ProjectDto dto,
                         BindingResult result,
                         @RequestParam(required = false) Long categoryId,
                         @RequestParam(required = false) List<Long> skillIds,
                         @RequestParam(value="image", required = false) MultipartFile image,
                         RedirectAttributes redirectAttributes,Authentication auth)
            throws IOException {


        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        Project project = projectService.getEntityById(id);

        if (!project.getUser().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN) {

            return "redirect:/projects";
        }

        if(result.hasErrors()){
            return "edit-project";
        }

        projectService.update(id, dto, image);

        if(categoryId != null){
            projectService.assignCategory(id, categoryId);
        }

        if(skillIds != null && !skillIds.isEmpty()){
            projectService.assignSkills(id, skillIds);
        }

        activityService.log("Project updated ");

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Project updated successfully"
        );

        return "redirect:/projects";
    }


    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model,Authentication auth) {

        AppUser user=userRepository.findByEmail(auth.getName()).orElseThrow();
        Project project=projectService.getEntityById(id);

        if (!project.getUser().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN) {
            return "redirect:/projects";
        }

        model.addAttribute("project", projectService.getById(id));
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("skills",skillService.getAll());




        return "edit-project";
    }


    @PostMapping ("/delete/{id}")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes,Authentication auth) {

        AppUser user=userRepository.findByEmail(auth.getName()).orElseThrow();
        Project project=projectService.getEntityById(id);


        if (!project.getUser().getId().equals(user.getId())
                && user.getRole() != Role.ADMIN) {
            return "redirect:/projects";
        }

        projectService.delete(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Project deleted successfully."
        );

        activityService.log("Project deleted");

        return "redirect:/projects";
    }



    @GetMapping("/categorize/{id}")
    public String categorizePage(@PathVariable Long id,Model model){
        model.addAttribute("project",projectService.getById(id));
        model.addAttribute("skills",skillService.getAll());
        model.addAttribute("categories",categoryService.getAll());
        return "categorize";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/categorize/{id}")
    public String saveCategorize(@PathVariable Long id,
                                 @RequestParam Long categoryId,
                                 @RequestParam (required = false)List<Long> skillId) {

        if (skillId == null || skillId.isEmpty()) {
            return "redirect:/projects/categorize/" + id + "?error";
        }

        projectService.assignCategory(id, categoryId);
        projectService.assignSkills(id, skillId);

        return "redirect:/projects";
    }

}