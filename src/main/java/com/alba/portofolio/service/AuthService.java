package com.alba.portofolio.service;

import com.alba.portofolio.dto.request.LoginRequest;
import com.alba.portofolio.dto.request.RegisterRequest;
import com.alba.portofolio.dto.response.LoginResponse;
import com.alba.portofolio.dto.response.RegisterResponse;
import com.alba.portofolio.entity.User;

public interface AuthService {



    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}

