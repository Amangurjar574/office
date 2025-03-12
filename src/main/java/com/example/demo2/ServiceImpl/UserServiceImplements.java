package com.example.demo2.ServiceImpl;

import com.example.demo2.Service.User_Service;
import com.example.demo2.entity.User_Detail;
import com.example.demo2.model.*;
import com.example.demo2.model_service.UserErrorSuccess;
import com.example.demo2.repo.userRepo;
import com.example.demo2.utility.DataSegmentationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImplements implements User_Service {

    @Autowired
    userRepo userRepo;
    @Autowired
    EmailSenderImplements emailSenderImplements;
   @Autowired
    PasswordEncoder passwordEncoder;


    private boolean isUserExists(User_Detail user) {
        return Optional.ofNullable(user).isPresent();
    }
    @Override
    public UserErrorSuccess createUser(CreateUserModel createUserModel) {
        log.info("Check if the user already exists by email");
        User_Detail user = userRepo.findByEmail(createUserModel.getEmail());
        if (isUserExists(user) && user.getEmail().equals(createUserModel.getEmail())) {
            log.error("Email already exists. Please use a different email...:"+createUserModel.getEmail());
            return new UserErrorModel("Email already exists. Please use a different email.",createUserModel.getEmail(),null); // Conflict condition
        }
        log.info("Create a new user..");
        User_Detail userDetail = new User_Detail();
        userDetail.setEmail(createUserModel.getEmail());
        userDetail.setName(createUserModel.getName());
        userDetail.setRole(createUserModel.getRole());

        String token=createToken();
        userDetail.setJwtforsetpassword(token);
        String tokenurl = crearteUrl(createUserModel.getEmail(),token); // url for set password

        emailSenderImplements.sendEmail(createUserModel.getEmail(), "Set your password", DataSegmentationConstants.messagetemplateFirst+tokenurl+DataSegmentationConstants.getMessagetemplateLast);

        User_Detail savedUser = userRepo.save(userDetail);
          if (isUserExists(savedUser) && savedUser.getUserid() > 0)
          {

            return new SuccessDetailsModel("User created successfully",createUserModel.getEmail());
          }
        else
        {
            return new UserErrorModel("Error occurred while saving user.",createUserModel.getEmail(),null);
        }
    }
    private String crearteUrl(String email,String token)
    {
        StringBuilder createUrl=new StringBuilder();
        createUrl.append("http://localhost:9098/v1/api/user/check/token");
        createUrl.append("?email="+email);
        createUrl.append("&token="+token);
        return createUrl.toString();
    }
    private String createToken()
    {
         return  passwordEncoder.encode(UUID.randomUUID().toString());
    }
    @Override
    public UserErrorSuccess deleteUser(long id) {

        User_Detail userDetail = userRepo.findByUserid(id);

        // Check if the user exists
        if (isUserExists(userDetail)) {
            if (!userDetail.isDeleted()) {
                userDetail.setDeleted(true);
                User_Detail savedUser = userRepo.save(userDetail);
                if (isUserExists(savedUser) && savedUser.getUserid() > 0){
                    return new SuccessDetailsModel("User successfully deleted","id:"+id);
                }
                else{
                    return new InternalServerErrorModel("Error occurred while saving user.");
                }
            } else {
                return new UserErrorModel("User is already deleted",""+id,null);
            }
        } else {
            return new UserDataNotFoundModel("User not found");
        }
    }

    @Override
    public UserErrorSuccess updateUser(UserEditDetailsModel userEdit_details_model) {
        // Find the user by the provided user ID
        User_Detail userDetail = userRepo.findByUserid(userEdit_details_model.getUserid());

        // Check if the user exists
        if (isUserExists(userDetail)) {
            // Update the fields only if the new data is provided
            userDetail.setName(userEdit_details_model.getName() != null ? userEdit_details_model.getName() : userDetail.getName());
            userDetail.setRole(userEdit_details_model.getRole() != null ? userEdit_details_model.getRole() : userDetail.getRole());
            userDetail.setUserid(userEdit_details_model.getUserid() <=0 ?userEdit_details_model.getUserid(): userDetail.getUserid());
            User_Detail savedUser = userRepo.save(userDetail);
            if (isUserExists(savedUser))
                return new SuccessDetailsModel("user successfully updated",userDetail.getEmail());
            else
                return new UserErrorModel("Error occurred while saving user.","id : "+userEdit_details_model.getUserid(),null);
        } else {
            // Return error message if user is not found
            return new UserErrorModel("data not found...","id : "+userEdit_details_model.getUserid(),null);
        }
    }

    @Override
    public User_Detail userSearch(long id) {
        User_Detail userDetail= userRepo.findByUserid(id);
        userDetail.setPassword("...");
        return userDetail;
    }

    @Override
    public UserErrorSuccess checkToken(String email, String token) {
        User_Detail userDetail = userRepo.findByEmail(email);
        if (isUserExists(userDetail)) {
            if (userDetail.getJwtforsetpassword().equals(token))
                return new SuccessDetailsModel("success", email);
            else
                return new UserErrorModel("not valid...", email, null);
        }
        else
            return new UserDataNotFoundModel("not found..");
    }

    @Override
    public UserErrorSuccess set_password(JwtRequest jwtRequest) {
        User_Detail userDetail = userRepo.findByEmail(jwtRequest.getEmail());

        if (isUserExists(userDetail)) {
            userDetail.setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
            userDetail.setStatus("Active");
            userDetail.setJwtforsetpassword(null);
            User_Detail savedUser = userRepo.save(userDetail);
            if (isUserExists(savedUser) && savedUser.getUserid() > 0) {
            return new SuccessDetailsModel("Your password has been successfully set, and you are now eligible to log in...",userDetail.getEmail());
            }
            else{
                return new InternalServerErrorModel("Error occurred while saving user.");}
        }
        else {
            return new UserDataNotFoundModel("data not found....email is wrong...");
        }
    }


}



/*
* else if(isUserExists(userDetail)&&userDetail.getStatus().equals("Active"))
        {
            if(userDetail.getJwtforsetpassword().equals(token[0]))
            {
                userDetail.setPassword(passwordEncoder.encode(password));
                User_Detail savedUser = userRepo.save(userDetail);
                if (isUserExists(savedUser) && savedUser.getUserid() > 0) {
                    return new Success_Details_Model("Your password has been successfully set, and you are now eligible to log in...",);
                }else{
                    return new Internal_Server_Error("Error occurred while saving user.");}
            }
            else{
                return new ErrorResponse("token is not valid..",userDetail.getEmail());
            }
        }
*
* */