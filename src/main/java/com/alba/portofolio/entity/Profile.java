package com.alba.portofolio.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    private String fullName;
    private String about;
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
