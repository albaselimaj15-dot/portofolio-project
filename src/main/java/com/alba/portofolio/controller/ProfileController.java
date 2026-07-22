package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Profile;

import com.alba.portofolio.service.ProfileService;
import com.alba.portofolio.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.io.IOException;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserService userService;
    private final ActivityService activityService;


    public ProfileController(ProfileService profileService,
                             UserService userService, ActivityService activityService
    ) {
        this.profileService = profileService;
        this.userService = userService;

        this.activityService = activityService;
    }

    @GetMapping
    public String profile(Authentication auth, Model model) {

        AppUser user = userService.findByEmail(auth.getName());

        model.addAttribute("loggedUser", user);

        Profile profile = profileService.getOrCreateProfile(auth.getName());


        model.addAttribute("profile", profile);

        return "profile";
    }

    @PostMapping("/upload-image")
    public String uploadProfileImage(
            @RequestParam("image") MultipartFile image,
            Authentication auth,
            RedirectAttributes redirectAttributes
    ) throws IOException {


        String result = profileService.updateProfileImage(
                auth.getName(),
                image
        );


        if(result != null){

            redirectAttributes.addFlashAttribute(
                    "error",
                    result
            );

        } else {

            activityService.log(
                    "Profile image updated by " + auth.getName()
            );


            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile image updated successfully!"
            );
        }


        return "redirect:/profile";
    }

    @GetMapping("/edit")
    public String editProfile(Authentication auth, Model model) {

        AppUser user = userService.findByEmail(auth.getName());

        Profile profile = user.getProfile();

        model.addAttribute("profile", profile);
        model.addAttribute("loggedUser", user);

        return "edit-profile";
    }

    @PostMapping("/update")
    public String updateProfile(
            @ModelAttribute Profile profile,
            @RequestParam("email") String email,
            @RequestParam(value = "image", required = false) MultipartFile image,
            RedirectAttributes redirectAttributes
    ) {


        if (profile.getFullName() == null || profile.getFullName().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "Full name is required."
            );

            return "redirect:/profile";
        }


        if (profile.getAbout() == null || profile.getAbout().trim().isEmpty()) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "About field is required."
            );

            return "redirect:/profile";
        }

        String result = profileService.updateProfile(email, profile, image);

        if (result != null) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    result
            );

        } else {

            redirectAttributes.addFlashAttribute(
                    "success",
                    "Profile updated successfully!"
            );
        }

        return "redirect:/profile";
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(@RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Authentication auth,
                                 RedirectAttributes redirectAttributes) {

        String result = profileService.changePassword(
                auth.getName(),
                currentPassword,
                newPassword,
                confirmPassword
        );


        switch (result) {

            case "SUCCESS":
                redirectAttributes.addFlashAttribute(
                        "success",
                        "Password updated successfully."
                );
                break;


            case "PASSWORD_TOO_SHORT":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Password must be at least 8 characters."
                );
                break;


            case "CURRENT_PASSWORD_REQUIRED":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Current password is required."
                );
                break;


            case "NEW_PASSWORD_REQUIRED":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "New password is required."
                );
                break;


            case "CONFIRM_PASSWORD_REQUIRED":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Confirm password is required."
                );
                break;


            case "CURRENT_PASSWORD_INCORRECT":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Current password is incorrect."
                );
                break;

            case "NEW_PASSWORD_SAME_AS_OLD":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "New password cannot be the same as old password."
                );
                break;


            case "PASSWORDS_DO_NOT_MATCH":
                redirectAttributes.addFlashAttribute(
                        "error",
                        "Passwords do not match."
                );
                break;
        }


        return "redirect:/profile";
    }
}