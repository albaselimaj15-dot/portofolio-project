package com.alba.portofolio.controller;

import com.alba.portofolio.entity.Skill;
import com.alba.portofolio.entity.User;
import com.alba.portofolio.repository.SkillRepository;
import com.alba.portofolio.repository.UserRepository;
import com.alba.portofolio.service.SkillService;
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

    public SkillPageController(SkillRepository skillRepository, UserRepository userRepository, SkillService skillService) {
        this.skillRepository = skillRepository;
        this.userRepository = userRepository;
        this.skillService = skillService;
    }

    @GetMapping
    public String getSkills(Model model, Authentication auth) {

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        model.addAttribute("skills", skillRepository.findByUserId(user.getId()));

        return "skills";
    }

    @PostMapping
    public String createSkill(@ModelAttribute Skill skill, Authentication auth) {

        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/login";
        }

        String email = auth.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        skill.setUser(user);

        skillRepository.save(skill);

        return "redirect:/skills";
    }
    @GetMapping("/edit/{id}")
    public String editSkill(@PathVariable Long id,Model model) {

      Skill skill=skillRepository.findById(id).get();

      model.addAttribute("skill",skill);

        return "edit-skill";
    }
    @PostMapping("/update")
    public String update(@ModelAttribute Skill skill,
                         Authentication auth) {

        String email = auth.getName();

        skillService.updateWithUser(skill, email);

        return "redirect:/skills";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        skillRepository.deleteById(id);

        return "redirect:/skills";
    }
}