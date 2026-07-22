package com.alba.portofolio.service;


import com.alba.portofolio.entity.AppUser;
import com.alba.portofolio.entity.Profile;
import com.alba.portofolio.repository.ProfileRepository;
import com.alba.portofolio.repository.UserRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;





@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final Cloudinary cloudinary;

    @Value("${storage.type}")
    private String storageType;



    public ProfileService(ProfileRepository profileRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, Cloudinary cloudinary) {
        this.profileRepository = profileRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.cloudinary = cloudinary;
    }



    public Profile getProfileByUser(String email) {

        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return profileRepository.findByUserEmail(email).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public Profile getOrCreateProfile(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return profileRepository.findByUserEmail(email)
                .orElseGet(() -> {
                    Profile profile = new Profile();
                    profile.setUser(user);
                    return profileRepository.save(profile);
                });
    }
    public String updateProfileImage(String email, MultipartFile image) throws IOException {


        Profile profile = profileRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));


        if(image == null || image.isEmpty()){

            return "No image selected.";
        }


        // CHECK SIZE
        if(image.getSize() > 5 * 1024 * 1024){

            return "Image size must be less than 5MB.";
        }


        // CHECK TYPE
        String contentType = image.getContentType();

        if(contentType == null || !contentType.startsWith("image/")){

            return "Only image files are allowed.";
        }



        if(storageType.equals("cloudinary")) {

            Map uploadResult = cloudinary.uploader().upload(
                    image.getBytes(),
                    ObjectUtils.emptyMap()
            );

            String imageUrl = uploadResult.get("secure_url").toString();

            profile.setProfileImage(imageUrl);


        } else {


            Path uploadPath = Paths.get("uploads", "profiles");

            Files.createDirectories(uploadPath);


            String fileName = UUID.randomUUID()
                    + "_"
                    + image.getOriginalFilename();


            Files.copy(
                    image.getInputStream(),
                    uploadPath.resolve(fileName),
                    StandardCopyOption.REPLACE_EXISTING
            );


            profile.setProfileImage(fileName);
        }


        profileRepository.save(profile);


        return null;
    }

    public String updateProfile(String email, Profile updatedProfile, MultipartFile image) {

        System.out.println("SERVICE FULLNAME: " + updatedProfile.getFullName());
        System.out.println("SERVICE ABOUT: " + updatedProfile.getAbout());

        Profile profile = getOrCreateProfile(email);

        profile.setFullName(updatedProfile.getFullName());
        profile.setAbout(updatedProfile.getAbout());



        if (image != null && !image.isEmpty()) {


            if (image.getSize() > 5 * 1024 * 1024) {
                return "Image size must be less than 5MB.";
            }

            String contentType = image.getContentType();

            if (contentType == null || !contentType.startsWith("image/")) {
                return "Only image files are allowed.";
            }

            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();

            Path uploadPath = Paths.get("uploads/profiles");

            try {

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(
                        image.getInputStream(),
                        uploadPath.resolve(fileName),
                        StandardCopyOption.REPLACE_EXISTING
                );


                profile.setProfileImage(fileName);


            } catch (IOException e) {
                throw new RuntimeException("Could not save profile image", e);
            }
        }


        profileRepository.save(profile);
        return null;
    }

    public String changePassword(String email, String oldPassword, String newPassword, String confirmPassword){

        if(oldPassword == null || oldPassword.isEmpty()){
            return "CURRENT_PASSWORD_REQUIRED";
        }

        if (newPassword.length() < 8) {
            return "PASSWORD_TOO_SHORT";
        }

        if(newPassword == null || newPassword.isEmpty()){
            return "NEW_PASSWORD_REQUIRED";
        }

        if(confirmPassword == null || confirmPassword.isEmpty()){
            return "CONFIRM_PASSWORD_REQUIRED";
        }
        AppUser user=userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

        if(!passwordEncoder.matches(oldPassword,user.getPassword())){
            return "CURRENT_PASSWORD_INCORRECT";
        }

        if(passwordEncoder.matches(newPassword, user.getPassword())){
            return "NEW_PASSWORD_SAME_AS_OLD";
        }

        if(!newPassword.equals(confirmPassword)){
            return "PASSWORDS_DO_NOT_MATCH";
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "PASSWORD_CHANGED";
}

public Profile save(Profile profile){
    return profileRepository.save(profile);
}
    }

