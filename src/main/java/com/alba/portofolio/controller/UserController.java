package com.alba.portofolio.controller;


import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.entity.AppUser;


import com.alba.portofolio.repository.UserRepository;
import com.alba.portofolio.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public UserController(UserService userService, UserRepository userRepository, ActivityService activityService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    @GetMapping
    public ResponseEntity<List<AppUser>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/profile/upload-image")
    public String uploadProfileImage(@RequestParam("image") MultipartFile image,
                                     Authentication auth) throws IOException {

        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        if (image != null && !image.isEmpty()) {

            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads", "profiles");
            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            image.transferTo(filePath.toFile());

            user.setImageUrl("/uploads/profiles/" + fileName);
        }

        userRepository.save(user);

        activityService.log("Profile image updated by " + auth.getName());

        return "redirect:/profile";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {

        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();

        model.addAttribute("profile", user);

        return "profile";
    }



}
