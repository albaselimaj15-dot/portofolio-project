package com.alba.portofolio.controller;

import com.alba.portofolio.dto.request.LoginRequest;
import com.alba.portofolio.dto.request.RegisterRequest;
import com.alba.portofolio.service.AuthService;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthPageController {

    private final AuthService authService;

    public AuthPageController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register-page")
    public String register(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password) {

        RegisterRequest request =
                new RegisterRequest(username, email, password);

        authService.register(request);

        return "redirect:/";
    }

    @PostMapping("/login-page")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                         HttpSession session ){

         LoginRequest request=new LoginRequest(email,password);

         authService.login(request);
        session.setAttribute("user", email);

         return "redirect:/dashboard";

    }
}
