package com.alba.portofolio.controller;

import com.alba.portofolio.dto.request.LoginRequest;
import com.alba.portofolio.dto.request.RegisterRequest;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.UserRepository;
import com.alba.portofolio.service.AuthService;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthPageController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthPageController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;

    }

    // REGISTER PAGE
    // =====================
    @GetMapping("/register")
    public String registerPage(Model model) {

        model.addAttribute("user",new AppUser());


        return "register";
    }

    // LOGIN PAGE
    // =====================
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("user") AppUser user,
                           @RequestParam String confirmPassword) {


        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            return "redirect:/register?error=username_empty";
        }


        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            return "redirect:/register?error=email_empty";
        }


        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            return "redirect:/register?error=password_empty";
        }


        if (!user.getPassword().equals(confirmPassword)) {
            return "redirect:/register?error=password_mismatch";
        }


        String normalizedEmail = user.getEmail().trim().toLowerCase();


        if (userRepository.existsByEmail(normalizedEmail)) {
            return "redirect:/register?error=email_exists";
        }


        RegisterRequest request = new RegisterRequest(
                user.getUsername(),
                normalizedEmail,
                user.getPassword()
        );


        authService.register(request);

        return "redirect:/login";
    }

}
