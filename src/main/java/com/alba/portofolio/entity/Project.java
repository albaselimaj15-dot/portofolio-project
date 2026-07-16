package com.alba.portofolio.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
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


    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Link is required")
    private String link;


    private String imageUrl;


    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false)
    private AppUser user;

    @ManyToOne(optional = false)
    @JoinColumn(name="category_id",nullable = false)
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "project_skills",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    @Override
    public String toString() {
        return "Project{id=" + id + ", title='" + title + "'}";
    }


}
