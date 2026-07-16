package com.alba.portofolio.entity;

import jakarta.persistence.*;

import lombok.Data;

@Data
@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)

    private Long id;

    
    private String fullName;

    private String about;

    private String profileImage;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_id",nullable = false,unique = true)
    private AppUser user;
}
