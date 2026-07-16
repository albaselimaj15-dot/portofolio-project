package com.alba.portofolio.service;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }



    @Override
    public AppUser findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
    }

    @Override
    public AppUser save(AppUser user){
        return userRepository.save(user);
    }



}
