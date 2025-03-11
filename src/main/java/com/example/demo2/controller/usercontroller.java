package com.example.demo2.controller;

import com.example.demo2.ServiceImpl.UserServiceImplements;
import com.example.demo2.entity.User_Detail;
import com.example.demo2.model.*;
import com.example.demo2.model_service.UserErrorSuccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/{version}/api/user")
public class usercontroller {

    @Autowired
    UserServiceImplements userServiceImplements;

    @DeleteMapping("/user")
    public ResponseEntity<String> deleteUser(@Param("userid") long userid) {
        if (userid > 0) {
            try {
                UserErrorSuccess result = userServiceImplements.deleteUser(userid);
                if (result instanceof SuccessDetailsModel) {
                    return new ResponseEntity<>("User successfully deleted", HttpStatus.OK); // 200 OK
                } else if (result instanceof UserErrorModel) {
                    return new ResponseEntity<>("User is already deleted", HttpStatus.BAD_REQUEST); // 400 Bad Request not a 409 conflict
                } else if (result instanceof UserDataNotFoundModel) {
                    return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND); // 404 Not Found
                } else {
                    return new ResponseEntity<>("Unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
                }
            } catch (Exception e) {
                return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } else {
            return new ResponseEntity<>("Please provide a valid user ID.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PostMapping("/user")
    public ResponseEntity<String> createUser(@RequestBody CreateUserModel createUserModel) {
        // Check if the required fields are present
        if (createUserModel.getName() != null && createUserModel.getEmail() != null && createUserModel.getRole() != null) {
            try {
                UserErrorSuccess result = userServiceImplements.createUser(createUserModel);
                if (result instanceof SuccessDetailsModel) {
                    log.info(((SuccessDetailsModel) result).getSuccess()+".:"+((SuccessDetailsModel) result).getDetails());
                    return new ResponseEntity<>(((SuccessDetailsModel) result).getSuccess(), HttpStatus.CREATED); // 201 Created
                } else if (result instanceof UserErrorModel) {
                    log.error(((UserErrorModel) result).getError()+".:"+((UserErrorModel) result).getDetails());
                    return new ResponseEntity<>(((UserErrorModel) result).getError(), HttpStatus.BAD_REQUEST); // 400 Bad Request
                } else {
                    // Catch-all for unexpected issues
                    return new ResponseEntity<>( "An unexpected server error occurred", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
                }
            }catch (Exception e) {
                return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } else {
            log.warn("Please provide valid data.");
            return new ResponseEntity<>("Please provide valid data.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @PutMapping("/user")
    public ResponseEntity<String> editUser(@RequestBody UserEditDetailsModel userEdit_details_model) {
        if (userEdit_details_model.getUserid() > 0) {
            try {
                 UserErrorSuccess result = userServiceImplements.updateUser(userEdit_details_model);
                if (result instanceof SuccessDetailsModel) {
                    return new ResponseEntity<>("user successfully updated", HttpStatus.OK); // 200 OK
                } else if (result instanceof UserErrorModel) {
                    return new ResponseEntity<>("data not found...", HttpStatus.NOT_FOUND); // 404 Not Found
                } else {
                    return new ResponseEntity<>("Unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
                }
            } catch (Exception e) {
                return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } else {
            return new ResponseEntity<>("Please provide a valid user ID.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }

    @GetMapping("/user")
    public ResponseEntity<User_Detail> getByUserid(@Param("userid") long userid) {
        if (userid > 0) {
            User_Detail userDetail = userServiceImplements.userSearch(userid);
            if (userDetail != null) {
                return new ResponseEntity<>(userDetail, HttpStatus.OK);// Return the user details with a 200 OK status
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);// User not found, return 404 Not Found
            }
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Invalid user ID provided, return 400 Bad Request
        }
    }

    @PatchMapping("/reset/password")
    public ResponseEntity<String> enable_Disble_User(@RequestBody UserResetPasswordModel userResetPassword) {

        if (userResetPassword.getEmail() == null || userResetPassword.getEmail().isEmpty() || userResetPassword.getPassword() == null || userResetPassword.getPassword().isEmpty()) {
            return new ResponseEntity<>("Email and password are required.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        try {
            UserErrorSuccess result = userServiceImplements.set_password(userResetPassword);
            if (result instanceof SuccessDetailsModel) {
                return new ResponseEntity<>("Your password has been successfully set, and you are now eligible to log in...", HttpStatus.OK); // 200 OK
            } else if (result instanceof UserDataNotFoundModel) {
                return new ResponseEntity<>("data not found....email is wrong...", HttpStatus.NOT_FOUND); // 404 Not Found
            } else {
                return new ResponseEntity<>("Unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    @GetMapping("/check/token")
    public ResponseEntity<String> checkToken(@Param("email") String email,@Param("token") String token)
    {
        UserErrorSuccess user_errorSuccess =userServiceImplements.checkToken(email,token);
            if(user_errorSuccess instanceof SuccessDetailsModel){
                HttpHeaders headers = new HttpHeaders();
                headers.add("Location", "emailsender");
                return new ResponseEntity<String>(headers, HttpStatus.FOUND);
//                return "redirect:/view/emailsender?email=" + email + "&token=" + token;
            }
            else{
                return new ResponseEntity<String>("", HttpStatus.FOUND);
            }
    }

}
