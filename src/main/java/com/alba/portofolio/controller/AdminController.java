package com.alba.portofolio.controller;

import com.alba.portofolio.repository.UserRepository;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")

    public String AdminController(Model model){

        model.addAttribute("users",userRepository.findAll());
        return "admin/users";
    }
}
