package com.alba.portofolio.controller;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Profile;

import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.*;

import com.alba.portofolio.service.ProfileService;

import org.springframework.security.core.Authentication;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class ViewController {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SkillRepository skillRepository;
    private  final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;


    public ViewController(UserRepository userRepository, PasswordEncoder passwordEncoder, ProjectRepository projectRepository, SkillRepository skillRepository, ProfileRepository profileRepository, ProfileService profileService, CategoryRepository categoryRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.categoryRepository = categoryRepository;
    }


    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        String email = auth.getName();

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow();

        model.addAttribute("user", user.getUsername());

        model.addAttribute("isAdmin",
                user.getRole() == Role.ADMIN);

        model.addAttribute("projectCount", projectRepository.count());
        model.addAttribute("skillCount", skillRepository.count());
        model.addAttribute("categoryCount",categoryRepository.count());

        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model){

        Profile profile = profileService.getProfileByUser(auth.getName());
        profile.setImageUrl("/images.jpg");



        model.addAttribute("profile", profile);

        return "profile";
    }


    }




