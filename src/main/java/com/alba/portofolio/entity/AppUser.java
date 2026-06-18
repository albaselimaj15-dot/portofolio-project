package com.alba.portofolio.entity;


import com.alba.portofolio.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table(name="users")
public class AppUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Skill> skills;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

}
