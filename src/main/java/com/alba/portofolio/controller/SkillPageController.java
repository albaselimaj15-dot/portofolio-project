package com.alba.portofolio.controller;

import com.alba.portofolio.Activity.ActivityService.ActivityService;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Skill;
import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.UserRepository;
import com.alba.portofolio.service.SkillService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/skills")
public class SkillPageController {


    private final UserRepository userRepository;
    private final SkillService skillService;
    private final ActivityService activityService;

    public SkillPageController( UserRepository userRepository, SkillService skillService, ActivityService activityService) {

        this.userRepository = userRepository;
        this.skillService = skillService;
        this.activityService = activityService;
    }
    @GetMapping
    public String getSkills(Model model, Authentication auth) {

        String email = auth.getName();
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        boolean isAdmin = user.getRole() == Role.ADMIN;


        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("skill", new Skill());
        model.addAttribute("skills", skillService.getAll());

        return "skills";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/create")
    public String createSkill(Model model, Authentication auth) {

        String email = auth.getName();

        AppUser user = userRepository.findByEmail(email)
                .orElseThrow();

        boolean isAdmin = user.getRole() == Role.ADMIN;

        model.addAttribute("skill", new Skill());
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("skills", skillService.getAll());

        return "skills";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveSkill(@Valid @ModelAttribute("skill") Skill skill, BindingResult result,
                            Authentication auth,Model model,
                            RedirectAttributes redirectAttributes) {


        if(result.hasErrors()){

            model.addAttribute("isAdmin", true);
            model.addAttribute("skills", skillService.getAll());

            return "skills";
        }


        AppUser user = userRepository.findByEmail(auth.getName())
                .orElseThrow();
        skill.setUser(user);

        skillService.save(skill);


        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Skill created successfully!"
        );
        return "redirect:/skills";
    }

    @GetMapping("/edit/{id}")
    public String editSkill(@PathVariable Long id, Model model, Authentication auth) {

        String email = auth.getName();
        AppUser user = userRepository.findByEmail(email).orElseThrow();

        boolean isAdmin = user.getRole() == Role.ADMIN;

        Skill skill = skillService.findById(id);

        model.addAttribute("skill", skill);
        model.addAttribute("isAdmin", isAdmin);

        return "edit-skill";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/update")
    public String update( @RequestParam Long id,@ModelAttribute Skill dto,
                         Authentication auth,  RedirectAttributes redirectAttributes) {

        String email = auth.getName();

        AppUser user=userRepository.findByEmail(email).orElseThrow();

        skillService.updateWithUser(id,dto,user);
        activityService.log( "Skill updated");
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Skill updated successfully"
        );


        return "redirect:/skills";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        skillService.deleteById(id);
        activityService.log( "Skill deleted");
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Skill deleted successfully"
        );

        return "redirect:/skills";
    }
}