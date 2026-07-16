package com.alba.portofolio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name="skills")

public class Skill {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message="Skill name is required")
    private String name;

    @NotBlank(message="Level is required")
    private String level;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToMany(mappedBy = "skills")
    private List<Project> projects = new ArrayList<>();
}
