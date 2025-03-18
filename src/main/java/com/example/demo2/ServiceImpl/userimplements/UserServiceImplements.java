package com.example.demo2.ServiceImpl.userimplements;

import com.example.demo2.Service.userservice.User_Service;
import com.example.demo2.ServiceImpl.emailimplements.EmailSenderImplements;
import com.example.demo2.entity.user_entitys.User_Detail;
import com.example.demo2.model.jwt.JwtRequest;
import com.example.demo2.model.servererror.InternalServerErrorModel;
import com.example.demo2.model.success.SuccessDetailsModel;
import com.example.demo2.model.userdetails.*;
import com.example.demo2.model_service.UserErrorSuccess;
import com.example.demo2.repo.userRepo;
import com.example.demo2.utility.DataSegmentationConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class UserServiceImplements implements User_Service {

    @Autowired
   public  userRepo userRepo;
    @Autowired
    EmailSenderImplements emailSenderImplements;
   @Autowired
    PasswordEncoder passwordEncoder;


    public boolean isUserExists(User_Detail user) {
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
        userDetail.setCreated_date(LocalDateTime.now());
        String token=createToken();
        userDetail.setJwtforsetpassword(token);
        String tokenurl = crearteUrl(createUserModel.getEmail(),token); // url for set password
        emailSenderImplements.sendEmail(createUserModel.getEmail(), "Set your password", DataSegmentationConstants.messagetemplateFirst+tokenurl+DataSegmentationConstants.getMessagetemplateLast);
        User_Detail savedUser = userRepo.save(userDetail);
          if (isUserExists(savedUser) && savedUser.getUserid() > 0)
          {

            return new SuccessDetailsModel("User created successfully",createUserModel.getEmail(),null);
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
        long expiryTime = System.currentTimeMillis() + (10 * 60 * 1000); // 10 minString
        String  encodedExpiry = Base64.getEncoder().encodeToString(String.valueOf(expiryTime).getBytes(StandardCharsets.UTF_8));
        createUrl.append("&expiry="+encodedExpiry);
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
                userDetail.setUpdate_date(LocalDateTime.now());
                User_Detail savedUser = userRepo.save(userDetail);
                if (isUserExists(savedUser) && savedUser.getUserid() > 0){
                    return new SuccessDetailsModel("User successfully deleted","id:"+id,null);
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
            userDetail.setUpdate_date(LocalDateTime.now());
            User_Detail savedUser = userRepo.save(userDetail);
            if (isUserExists(savedUser))
                return new SuccessDetailsModel("user successfully updated",userDetail.getEmail(),null);
            else
                return new UserErrorModel("Error occurred while saving user.","id : "+userEdit_details_model.getUserid(),null);
        } else {
            // Return error message if user is not found
            return new UserErrorModel("data not found...","id : "+userEdit_details_model.getUserid(),null);
        }
    }
    @Override
    public UserErrorSuccess getUserById(long id) {
        log.info("🔍 Starting user search for ID: {}", id);

        try {
            User_Detail userDetail = userRepo.findByUserid(id);

            if (isUserExists(userDetail)&&!userDetail.isDeleted()) {
                log.info("✅ User found in database for ID: {}", id);

                UserSearchData data = new UserSearchData(
                        userDetail.getName(),
                        userDetail.getEmail(),
                        userDetail.getRole(),
                        userDetail.getUserid(),
                        userDetail.getStatus(),
                        userDetail.isDeleted()
                );
                log.debug(" User Data Prepared: {}", data);
                return new SuccessDetailsModel("User found successfully", "Id:"+id,data);
            } else {
                log.warn(" No user found for ID: {}", id);
                return new UserDataNotFoundModel("User not found for ID: " + id);
            }

        } catch (Exception e) {
            log.error(" Exception occurred while searching user for ID: {} | Message: {}", id, e.getMessage(), e);
            return new UserErrorModel("Internal Server Error", "Failed to fetch user", new String[]{e.getMessage()});
        }
    }

    @Override
    public UserErrorSuccess checkToken(String email, String token) {
        log.info("Checking token for email: {}", email);

        User_Detail userDetail = userRepo.findByEmail(email);

        // Null or not found check
        if (!isUserExists(userDetail )) {
            log.warn("User not found with email: {}", email);
            return new UserDataNotFoundModel(" User not found.");
        }

        // Token match check
        if (userDetail.getJwtforsetpassword().equals(token)) {
            log.info("Token matched for email: {}", email);
            return new SuccessDetailsModel(" Token verified successfully", email,null);
        } else {
            log.warn(" Invalid token for email: {}", email);
            return new UserErrorModel(" Invalid or expired token", email, null);
        }
    }
    @Override
    public UserErrorSuccess getAllUser() {
        log.info("🔍 Starting to fetch all users from database");

        try {
            List<User_Detail> userDetails = userRepo.findAll();

            if (userDetails != null && !userDetails.isEmpty()) {
                log.info(" User found..");
                List<UserSearchData> userList = new ArrayList<>();

                for (User_Detail user : userDetails) {
                if(!user.isDeleted())
                {
                    UserSearchData data = new UserSearchData(
                            user.getName(),
                            user.getEmail(),
                            user.getRole(),
                            user.getUserid(),
                            user.getStatus(),
                            user.isDeleted()

                    );
                    userList.add(data);
                }

                }
                log.debug(" Prepared User Data: {}", userList);

                return new SuccessDetailsModel(" All users fetched successfully", "Total: " + userList.size(), userList);
            } else {
                log.warn(" No users found in the database.");
                return new UserDataNotFoundModel("No users found.");
            }

        } catch (Exception e) {
            log.error(" Exception occurred while fetching users | Message: {}", e.getMessage(), e);
            return new UserErrorModel("Internal Server Error", "Failed to fetch user list", new String[]{e.getMessage()});
        }
    }
    @Override
    public UserErrorSuccess set_password(JwtRequest jwtRequest) {
        User_Detail userDetail = userRepo.findByEmail(jwtRequest.getEmail());

        if (isUserExists(userDetail)) {
            userDetail.setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
            userDetail.setStatus("Active");
            userDetail.setJwtforsetpassword(null);
//            userDetail.setUpdate_date(LocalDate.now());
//            userDetail.getUpdate_date(LocalDateTime.now());
            User_Detail savedUser = userRepo.save(userDetail);

            if (isUserExists(savedUser) && savedUser.getUserid() > 0) {
            return new SuccessDetailsModel("Your password has been successfully set, and you are now eligible to log in...",userDetail.getEmail(),null);
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