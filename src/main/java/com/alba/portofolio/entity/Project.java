package com.alba.portofolio.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Data
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



    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;


}
