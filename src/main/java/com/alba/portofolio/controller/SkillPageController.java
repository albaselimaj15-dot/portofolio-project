package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Skill;
import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.SkillRepository;
import com.alba.portofolio.repository.UserRepository;
import com.alba.portofolio.service.SkillService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/skills")
public class SkillPageController {

    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final SkillService skillService;
    private final ActivityService activityService;

    public SkillPageController(SkillRepository skillRepository, UserRepository userRepository, SkillService skillService, ActivityService activityService) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.skillService = skillService;
        this.activityService = activityService;
    }

    @GetMapping
    public String getSkills(Model model, Authentication auth) {

        String email = auth.getName();
        AppUser user = userRepository.findByEmail(email).orElseThrow();
        if (user.getRole() == Role.ADMIN) {
            model.addAttribute("skills", skillRepository.findAll());
        } else {
            model.addAttribute("skills", skillRepository.findByUserId(user.getId()));
        }


        return "skills";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public String createSkill(@ModelAttribute Skill skill, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        skill.setUser(user);

        skillRepository.save(skill);
        activityService.log("Skill created");

        return "redirect:/skills";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/edit/{id}")
    public String editSkill(@PathVariable Long id,Model model) {

      Skill skill=skillRepository.findById(id).get();

      model.addAttribute("skill",skill);


        return "edit-skill";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String update(@ModelAttribute Skill skill,
                         Authentication auth) {

        String email = auth.getName();

        skillService.updateWithUser(skill, email);
        activityService.log( "Skill updated");

        return "redirect:/skills";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        skillRepository.deleteById(id);
        activityService.log( "Skill deleted");

        return "redirect:/skills";
    }
}