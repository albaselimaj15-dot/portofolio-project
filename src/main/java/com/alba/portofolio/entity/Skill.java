package com.alba.portofolio.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name="skills")
public class Skill {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    @NotBlank
    private String name;
    private String level;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
