package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityModel.Activity;
import com.alba.portofolio.Activity.ActivityService.ActivityService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.UUID;


@Controller
public class ViewController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SkillRepository skillRepository;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final ProjectRepository projectRepository;
    private final CategoryRepository categoryRepository;
    private final ActivityService activityService;

    public ViewController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          ProjectRepository projectRepository,
                          SkillRepository skillRepository,
                          ProfileRepository profileRepository,
                          ProfileService profileService,
                          CategoryRepository categoryRepository,
                          ActivityService activityService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
        this.skillRepository = skillRepository;
        this.profileRepository = profileRepository;
        this.profileService = profileService;
        this.categoryRepository = categoryRepository;
        this.activityService = activityService;
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

        Profile profile=new Profile();
        profile.setUser(user);
        profileRepository.save(profile);

        return "redirect:/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        model.addAttribute("user", user.getUsername());
        model.addAttribute("isAdmin", user.getRole() == Role.ADMIN);

        model.addAttribute("projectCount", projectRepository.count());
        model.addAttribute("skillCount", skillRepository.count());
        model.addAttribute("categoryCount", categoryRepository.count());
        model.addAttribute("activityCount", activityService.count());

        model.addAttribute("activities", activityService.getLatest());

        return "dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication auth, Model model){

        AppUser appUser = userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Profile profile = profileRepository.findByUserId(appUser.getId())
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUser(appUser);
                    return p;
                });

        model.addAttribute("profile", profile);


        return "profile";
    }

    @PostMapping("/profile/upload-image")
    public String uploadProfileImage(@RequestParam("image") MultipartFile image,
                                     Authentication auth) throws IOException {

        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setUser(user);
                    return profileRepository.save(p);
                });

        if(image==null|| image.isEmpty()){
            throw new RuntimeException("No image selected");
        }


        if(profile.getImageUrl()!=null) {
             Path oldPath = Paths.get(System.getProperty("user.dir"),
                    profile.getImageUrl().replace("/uploads/profiles/", "uploads/profiles/"));

            Files.deleteIfExists(oldPath);

        }

        if (image != null && !image.isEmpty()) {

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profiles");
            Files.createDirectories(uploadPath);

            Files.copy(
                    image.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );



            profile.setImageUrl(fileName);
        }

        profileRepository.save(profile);

        return "redirect:/profile";
    }
}



