package com.example.demo2.Service;

import com.example.demo2.entity.User_Detail;
import com.example.demo2.model.CreateUserModel;
import com.example.demo2.model.JwtRequest;
import com.example.demo2.model.UserEditDetailsModel;
import com.example.demo2.model_service.UserErrorSuccess;

public interface User_Service {
    UserErrorSuccess createUser(CreateUserModel createUserModel);
    UserErrorSuccess deleteUser(long id);
    UserErrorSuccess updateUser(UserEditDetailsModel userEdit_details_model);
    UserErrorSuccess set_password(JwtRequest jwtRequest);
    User_Detail userSearch(long id);
    UserErrorSuccess checkToken(String email, String token);
}
