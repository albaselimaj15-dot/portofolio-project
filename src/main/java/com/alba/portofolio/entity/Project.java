package com.alba.portofolio.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;


    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String link;

    private String imageUrl;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name="project_skills",
            joinColumns =@JoinColumn(name="project_id"),
            inverseJoinColumns = @JoinColumn(name="skill_id")
    )
    private List<Skill> skills;

    @Override
    public String toString() {
        return "Project{id=" + id + ", title='" + title + "'}";
    }


}
