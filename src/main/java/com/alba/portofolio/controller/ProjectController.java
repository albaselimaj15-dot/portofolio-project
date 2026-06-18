package com.alba.portofolio.controller;

import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;
import com.alba.portofolio.entity.User;


import com.alba.portofolio.repository.CategoryRepository;
import com.alba.portofolio.repository.ProjectRepository;
import com.alba.portofolio.repository.UserRepository;

import com.alba.portofolio.service.ProjectService;
import org.springframework.security.core.Authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectService projectService;
    private final CategoryRepository categoryRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository, ProjectService projectService, CategoryRepository categoryRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.projectService = projectService;
        this.categoryRepository = categoryRepository;
    }
    @GetMapping
    public String getProjects(@RequestParam(required = false) String category,@RequestParam(required=false)
                              String search,
                              Model model,
                              Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        List<Project> projects;

        if(search!=null&&!search.isBlank()){
            System.out.println("SEARCH="+search);
            projects=projectRepository.findAllByUserAndTitleContainingIgnoreCase(user,search);
            System.out.println("PROJECTS FOUND="+projects.size());
        }

        else if (category != null&&!category.isBlank()) {
            Long categoryId = Long.parseLong(category);

            Category cat = categoryRepository.findById(categoryId)
                    .orElseThrow();

            projects = projectRepository.findAllByUserAndCategory(user, cat);
        } else {
            projects = projectRepository.findAllByUser(user);
        }

        model.addAttribute("projects", projects);
        model.addAttribute("categories",categoryRepository.findAll());
        return "projects";
    }
    @GetMapping("/categories")
    public String categoriesPage(Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        return "categories";
    }
    @GetMapping("/categorize/{id}")
    public String categorizePage(@PathVariable Long id, Model model) {

        Project project = projectService.findById(id);

        model.addAttribute("project", project);
        model.addAttribute("categories", categoryRepository.findAll());

        return "categorize";
    }
    @PostMapping("/categorize/{id}")
    public String updateCategory(@PathVariable Long id,
                             @RequestParam Long categoryId) {

        Category category=categoryRepository.findById(categoryId).orElseThrow();

        projectService.updateCategory(id, category);
        return "redirect:/projects";
    }
    @PostMapping
    public String create(@ModelAttribute Project project,  @RequestParam Long categoryId,Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }
        User user=userRepository.findByEmail(auth.getName()).orElseThrow();
      Category category=categoryRepository.findById(categoryId).orElseThrow();
      project.setUser(user);
        project.setCategory(category);
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