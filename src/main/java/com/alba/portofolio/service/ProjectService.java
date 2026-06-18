package com.alba.portofolio.service;

import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;
import com.alba.portofolio.entity.User;
import com.alba.portofolio.repository.ProjectRepository;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;


    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Project add(Project project) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        project.setUser(user);

        return projectRepository.save(project);
    }

    public void update(Long id, Project newproject) {
        Project existing = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Skill not found"));

        existing.setDescription(newproject.getDescription());
        existing.setLink(newproject.getLink());
        existing.setTitle(newproject.getTitle());
        existing.setCategory(newproject.getCategory());

        projectRepository.save(existing);
    }

    public void save(Project project, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found: " + email));

        project.setUser(user);

        projectRepository.save(project);
    }

    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    public Project getById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public void delete(long id) {
        projectRepository.deleteById(id);
    }


    public Project findById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public void updateCategory(Long id, Category category) {
        Project project=projectRepository.findById(id).orElseThrow(()->new RuntimeException("Project not found"));
        project.setCategory(category);
        projectRepository.save(project);

    }
}