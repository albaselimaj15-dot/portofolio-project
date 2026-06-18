package com.alba.portofolio.service;

import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Profile;
import com.alba.portofolio.repository.ProfileRepository;
import com.alba.portofolio.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;


    public ProfileService(ProfileRepository profileRepository, UserRepository userRepository) {
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    public Profile getProfileByUser(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return profileRepository.findByUserId(user.getId()).orElse(new Profile());
    }
}
