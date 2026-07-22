package com.alba.portofolio.entity;


import com.alba.portofolio.Activity.ActivityModel.Activity;
import com.alba.portofolio.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;


import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users",uniqueConstraints = {@UniqueConstraint(columnNames = "email"), @UniqueConstraint(columnNames = "username")
})
public class AppUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Invalid email format")
    @Email(message = "Email is required")
    private String email;


     @NotBlank(message = "Password is required")
    private String password;

     @NotNull(message="Role si required")
    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Skill> skills;

    @OneToMany(mappedBy = "user")
    private List<Project> projects;

    @OneToMany(mappedBy ="user" )
    private List<Activity>activities;

    @OneToOne(mappedBy = "user",cascade =CascadeType.ALL)
    private Profile profile;

    @Override
    public String toString() {
        return "AppUser{id=" + id + ", email='" + email + "'}";
    }

}
