package com.alba.portofolio.Config;


import com.alba.portofolio.repository.UserRepository;
import com.alba.portofolio.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        //PUBLIC
                        .requestMatchers("/login", "/register", "/css/**", "/img/**", "/error", "/portofolio",
                                "/portofolio/**",
                                "/uploads/**").permitAll()

                        //ADMIN ONLY
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        //AUTHENTICATED USERS

                        .requestMatchers("/dashboard", "/profile/**", "/projects/**", "/skills/**")
                        .hasAnyRole("USER", "ADMIN")

                        //EVERYTHING ELSE

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .build();



    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}