package com.example.demo2.Service.userservice;

import com.example.demo2.model.userdetails.CreateUserModel;
import com.example.demo2.model.jwt.JwtRequest;
import com.example.demo2.model.userdetails.UserEditDetailsModel;
import com.example.demo2.model_service.UserErrorSuccess;

public interface User_Service {
    UserErrorSuccess createUser(CreateUserModel createUserModel);
    UserErrorSuccess deleteUser(long id);
    UserErrorSuccess updateUser(UserEditDetailsModel userEdit_details_model);
    UserErrorSuccess set_password(JwtRequest jwtRequest);
    UserErrorSuccess getUserById(long id);
    UserErrorSuccess checkToken(String email, String token);
    UserErrorSuccess getAllUser();
}
