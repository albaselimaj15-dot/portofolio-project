package com.alba.portofolio.service;

import com.alba.portofolio.Activity.ActivityModel.Activity;
import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.dto.ProjectDto;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Category;
import com.alba.portofolio.entity.Project;
import com.alba.portofolio.entity.Skill;
import com.alba.portofolio.repository.CategoryRepository;
import com.alba.portofolio.repository.ProjectRepository;
import com.alba.portofolio.repository.SkillRepository;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final CategoryRepository categoryRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          SkillRepository skillRepository,
                          CategoryRepository categoryRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.categoryRepository = categoryRepository;
    }

    // CREATE
    public Project createProject(ProjectDto dto,
                                 Long categoryId,
                                 List<Long> skillIds,
                                 MultipartFile image,
                                 String email) throws IOException {

        AppUser user = userRepository.findByEmail(email).orElseThrow();

        Category category = categoryRepository.findById(categoryId).orElseThrow();

        List<Skill> skills = new ArrayList<>();

        if (skillIds != null && !skillIds.isEmpty()) {
            skills = skillRepository.findAllById(skillIds);
        }

        Project project = new Project();
        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setLink(dto.getLink());
        project.setUser(user);
        project.setCategory(category);


        project.setSkills(skills);


        // IMAGE UPLOAD
        if (image != null && !image.isEmpty()) {

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "projects");
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            image.transferTo(filePath.toFile());

            project.setImageUrl("/uploads/projects/" + fileName);
        }

        return projectRepository.save(project);
    }

    // UPDATE
    public void update(ProjectDto dto) {

        Project project = projectRepository.findById(dto.getId())
                .orElseThrow();

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow();

        project.setTitle(dto.getTitle());
        project.setDescription(dto.getDescription());
        project.setLink(dto.getLink());
        project.setCategory(category);

        List<Skill> skills = new ArrayList<>();

        if (dto.getSkillIds() != null && !dto.getSkillIds().isEmpty()) {
            skills = skillRepository.findAllById(dto.getSkillIds());
        }

        project.setSkills(skills);

        projectRepository.save(project);
    }

    // FILTER
    public List<Project> filterPublic(String search, Long categoryId, List<Long> skillIds) {

        List<Project> projects = projectRepository.findAll();

        if (search != null && !search.isBlank()) {
            projects = projects.stream()
                    .filter(p -> p.getTitle().toLowerCase().contains(search.toLowerCase()))
                    .toList();
        }

        if (categoryId != null) {
            projects = projects.stream()
                    .filter(p -> p.getCategory() != null &&
                            p.getCategory().getId().equals(categoryId))
                    .toList();
        }

        if (skillIds != null && !skillIds.isEmpty()) {

            projects = projects.stream()
                    .filter(p -> p.getSkills() != null &&
                            p.getSkills().stream()
                                    .anyMatch(s -> skillIds.contains(s.getId())))
                    .toList();
        }

        return projects;
    }

    // GET ALL DTO
    public List<ProjectDto> getAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    // GET BY ID
    public ProjectDto getById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow();

        return mapToDto(project);
    }

    // DELETE
    public void delete(Long id) {
        projectRepository.deleteById(id);
    }

    // MAPPER
    private ProjectDto mapToDto(Project project) {

        ProjectDto dto = new ProjectDto();

        dto.setId(project.getId());
        dto.setTitle(project.getTitle());
        dto.setDescription(project.getDescription());
        dto.setLink(project.getLink());

        if (project.getSkills() != null) {
            dto.setSkillIds(
                    project.getSkills()
                            .stream()
                            .map(Skill::getId)
                            .toList()
            );
        }

        return dto;
    }

    // ASSIGN SKILLS
    public void assignSkills(Long projectId, List<Long> skillIds) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow();

        List<Skill> skills = new ArrayList<>();

        if (skillIds != null && !skillIds.isEmpty()) {
            skills = skillRepository.findAllById(skillIds);
        }

        project.setSkills(skills);
        projectRepository.save(project);
    }
}