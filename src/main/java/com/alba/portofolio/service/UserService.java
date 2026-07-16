package com.alba.portofolio.service;

import com.alba.portofolio.entity.AppUser;


import java.util.List;

public interface UserService {
    List<AppUser> getAllUsers();

    AppUser findByEmail(String email);

    AppUser save(AppUser user);
}
