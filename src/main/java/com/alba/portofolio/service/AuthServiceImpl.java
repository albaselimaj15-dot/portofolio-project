package com.alba.portofolio.service;

import com.alba.portofolio.dto.request.LoginRequest;
import com.alba.portofolio.dto.request.RegisterRequest;
import com.alba.portofolio.dto.response.LoginResponse;
import com.alba.portofolio.dto.response.RegisterResponse;
import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.enums.Role;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegisterResponse register(RegisterRequest request) {

        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        AppUser savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        AppUser dbUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), dbUser.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        return new LoginResponse(
                "TOKEN_WILL_GO_HERE",
                dbUser.getId(),
                dbUser.getEmail(),
                dbUser.getUsername(),
                "Login successful"
        );
    }
}
