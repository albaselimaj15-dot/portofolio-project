package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.entity.AppUser;

import com.alba.portofolio.entity.Profile;
import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.*;

import com.alba.portofolio.service.*;


import org.springframework.security.core.Authentication;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class DashboardController {

    private final SkillService skillService;
    private final ProjectService projectService;
    private final CategoryService categoryService;
    private final ActivityService activityService;
    private final UserService userService;
;


    public DashboardController(
            SkillService skillService,
            ProjectService projectService,
            CategoryService categoryService,
            ActivityService activityService,
            UserService userService) {

        this.skillService = skillService;
        this.projectService = projectService;
        this.categoryService = categoryService;
        this.activityService = activityService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication auth, Model model) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }


        AppUser loggedUser = userService.findByEmail(auth.getName());


        Profile profile = loggedUser.getProfile();


        if (profile == null) {
            profile = new Profile();
            profile.setUser(loggedUser);
        }


        loggedUser.setProfile(profile);


        model.addAttribute("loggedUser", loggedUser);

        model.addAttribute("user", loggedUser.getUsername());

        model.addAttribute("isAdmin", loggedUser.getRole() == Role.ADMIN);


        long projectCount = projectService.count();
        long skillCount = skillService.count();
        long categoryCount = categoryService.count();


        model.addAttribute("projectCount", projectCount);
        model.addAttribute("skillCount", skillCount);
        model.addAttribute("categoryCount", categoryCount);


        model.addAttribute("activities", activityService.getLatest());


        List<String> notifications = new ArrayList<>();

        if (projectCount == 0) {
            notifications.add("No projects available");
        }

        if (skillCount < 3) {
            notifications.add("Consider adding more skills");
        }

        if (categoryCount == 0) {
            notifications.add("Create at least one category");
        }


        model.addAttribute("notifications", notifications);


        return "dashboard";
    }
}