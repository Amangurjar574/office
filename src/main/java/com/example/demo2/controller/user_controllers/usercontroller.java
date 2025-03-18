package com.example.demo2.controller.user_controllers;

import com.example.demo2.ServiceImpl.userimplements.UserServiceImplements;
import com.example.demo2.model.jwt.JwtRequest;
import com.example.demo2.model.success.SuccessDetailsModel;
import com.example.demo2.model.userdetails.CreateUserModel;
import com.example.demo2.model.userdetails.UserDataNotFoundModel;
import com.example.demo2.model.userdetails.UserEditDetailsModel;
import com.example.demo2.model.userdetails.UserErrorModel;
import com.example.demo2.model_service.UserErrorSuccess;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/{version}/api/user")
public class usercontroller {

    @Autowired
    UserServiceImplements userServiceImplements;

    @DeleteMapping("/user")
    @Operation(summary = "For delete User By Id.")
    public ResponseEntity<Object> deleteUser(@RequestParam("userid") long userid) {
        if (userid > 0) {
            try {
                UserErrorSuccess result = userServiceImplements.deleteUser(userid);
                if (result instanceof SuccessDetailsModel) {
                    log.info("User deleted successfully: " + ((SuccessDetailsModel) result).getSuccess() + ": " + ((SuccessDetailsModel) result).getDetails());
                    return new ResponseEntity<>(result, HttpStatus.OK); // 200 OK
                } else if (result instanceof UserErrorModel) {
                    log.warn("Error deleting user: " + ((UserErrorModel) result).getError() + ": " + ((UserErrorModel) result).getDetails());
                    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST); // 400 Bad Request
                } else if (result instanceof UserDataNotFoundModel) {
                    log.warn("User not found: " + ((UserDataNotFoundModel) result).getError());
                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); // 404 Not Found
                } else {
                    log.error("Internal server error while deleting user: " + result);
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
                }
            } catch (Exception e) {
                log.error("An unexpected error occurred while deleting user: " + e.getMessage(), e);
                return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } else {
            log.warn("Invalid user ID provided for deletion: " + userid);
            return new ResponseEntity<>("Please provide a valid user ID.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
    @PostMapping("/user")
    @Operation(summary = "For creating a new user")
    public ResponseEntity<Object> createUser(@RequestBody CreateUserModel createUserModel) {
        // Check if the required fields are present
        if (createUserModel.getName() != null &&createUserModel.getName().length()>=1 && createUserModel.getEmail().endsWith("@gmail.com")&& createUserModel.getEmail().length()>=11 && createUserModel.getEmail() != null && createUserModel.getRole() != null &&createUserModel.getRole().length()>=4) {
            try {
                UserErrorSuccess result = userServiceImplements.createUser(createUserModel);
                // Handle success result
                if (result instanceof SuccessDetailsModel) {
                    SuccessDetailsModel successDetails = (SuccessDetailsModel) result;
                    log.info("User created successfully: " + successDetails.getSuccess() + " : " + successDetails.getDetails());
                    return new ResponseEntity<>(successDetails, HttpStatus.CREATED); // 201 Created
                }
                // Handle user error (e.g., invalid data)
                else if (result instanceof UserErrorModel) {
                    UserErrorModel userError = (UserErrorModel) result;
                    log.warn("Error creating user: " + userError.getError() + " : " + userError.getDetails());
                    return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST); // 400 Bad Request
                }
                else {
                    log.error("Unexpected server error while creating user: " + result);
                    return new ResponseEntity<>("An unexpected server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
                }
            } catch (Exception e) {
                log.error("An unexpected error occurred during user creation: ", e);
                return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } else {

            log.warn("Missing required user data (name, email, or role).");
            return new ResponseEntity<>("Please provide valid data.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
    @PatchMapping("/user/edit")
    @Operation(summary ="for update/Edit User")
    public ResponseEntity<Object> editUser(@RequestBody UserEditDetailsModel userEdit_details_model) {
        if (userEdit_details_model.getUserid() > 0) {
            try {
                 UserErrorSuccess result = userServiceImplements.updateUser(userEdit_details_model);
                if (result instanceof SuccessDetailsModel) {
                    log.info(((SuccessDetailsModel) result).getSuccess(),HttpStatus.OK);
                    return new ResponseEntity<>(result, HttpStatus.OK); // 200 OK
                } else if (result instanceof UserErrorModel) {
                    log.warn(((UserErrorModel) result).getError(),HttpStatus.NOT_FOUND);
                    return new ResponseEntity<>(result, HttpStatus.NOT_FOUND); // 404 Not Found
                } else {
                    log.error(((UserErrorModel) result).getError(),HttpStatus.INTERNAL_SERVER_ERROR);
                    return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
                }
            } catch (Exception e) {
                log.error("An unexpected error occurred : ",HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } else {
            log.error("Please provide a valid user ID.",HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("Please provide a valid user ID.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
    }
    @GetMapping("/user/id")
    @Operation(summary = "get user by ID")
    public ResponseEntity<Object> getUserById(@Param("userid") long userid) {
        try {
            if (userid <= 0) {
                log.warn(" Invalid user ID provided: {}", userid);
                return new ResponseEntity<>("Invalid user ID", HttpStatus.BAD_REQUEST);//400
            }
            UserErrorSuccess response = userServiceImplements.getUserById(userid);

            if (response instanceof SuccessDetailsModel) {
                log.info(" User found successfully for ID: {}", userid);
                return new ResponseEntity<>(response, HttpStatus.OK);//200
            } else if (response instanceof UserDataNotFoundModel) {
                log.warn(" User not found for ID: {}", userid);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//404
            } else {
                log.error("️ Unknown response received while fetching user for ID: {}", userid);
                return new ResponseEntity<>("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);//500
            }
        } catch (Exception e) {
            log.error(" Exception occurred while searching user by ID {}: {}", userid, e.getMessage(), e);
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//500
        }
    }
    @GetMapping("/user")
    @Operation(summary = "get  All user ")
    public ResponseEntity<Object> getAllUser() {
        try {

            UserErrorSuccess response = userServiceImplements.getAllUser();

            if (response instanceof SuccessDetailsModel) {
                log.info(" User found successfully for ID: ",response);
                return new ResponseEntity<>(response, HttpStatus.OK);//200
            } else if (response instanceof UserDataNotFoundModel) {
                log.warn(" User not found for ID: {}", response);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);//404
            } else {
                log.error("️ Unknown response received while fetching user for ID: {}", response);
                return new ResponseEntity<>("Unknown error occurred", HttpStatus.INTERNAL_SERVER_ERROR);//500
            }
        } catch (Exception e) {
            log.error(" Exception occurred while searching user by ID {}: {}",null, e.getMessage(), e);
            return new ResponseEntity<>("Internal Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//500
        }
    }

    @PatchMapping("/reset/password")
    @Operation(summary ="for reset and set password of user")
    public ResponseEntity<String> enable_Disble_User(@RequestBody JwtRequest jwtRequest) {

        if (jwtRequest.getEmail() == null || jwtRequest.getEmail().isEmpty() || jwtRequest.getPassword() == null || jwtRequest.getPassword().isEmpty()) {
          log.warn("Email and password are required : ", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>("Email and password are required.", HttpStatus.BAD_REQUEST); // 400 Bad Request
        }
        try {
            UserErrorSuccess result = userServiceImplements.set_password(jwtRequest);
            if (result instanceof SuccessDetailsModel) {
                log.info(((SuccessDetailsModel) result).getSuccess()+" : "+HttpStatus.OK);
                return new ResponseEntity<>("Your password has been successfully set, and you are now eligible to log in...", HttpStatus.OK); // 200 OK
            } else if (result instanceof UserDataNotFoundModel) {
                log.warn(((UserDataNotFoundModel) result).getError()+" : "+HttpStatus.NOT_FOUND);
                return new ResponseEntity<>("data not found....email is wrong...", HttpStatus.NOT_FOUND); // 404 Not Found
            } else {
                log.error("Unexpected error occurred. : "+HttpStatus.INTERNAL_SERVER_ERROR);
                return new ResponseEntity<>("Unexpected error occurred.", HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
            }
        } catch (Exception e) {
            log.error("An unexpected error occurred : "+HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }
    @RequestMapping("/check/token")
    @Operation(summary = "For checking if token is valid or not")
    public ModelAndView checkToken(@RequestParam("email") String email,
                                   @RequestParam("token") String token,
                                   @RequestParam("expiry") String expiry) {
        ModelAndView model = new ModelAndView();

        try {
            // Decode and parse expiry
            String decodedExpiryStr = new String(Base64.getDecoder().decode(expiry), StandardCharsets.UTF_8);
            long expiryTime = Long.parseLong(decodedExpiryStr);
            // Log expiry time
            log.info("Checking token for email: {}, expiryTime: {}", email, expiryTime);

            // Check if token is expired
            if (System.currentTimeMillis() > expiryTime) {
                log.warn("Link expired for email: {}", email);
//                model.setViewName("expired.jsp");//we can do it
                model.addObject("message", " Link expired, please request a new one.");
                return model;
            }

            // Validate token
            UserErrorSuccess result = userServiceImplements.checkToken(email, token);

            if (result instanceof SuccessDetailsModel) {
                log.info("Token verified successfully for email: {}", email);
                model.setViewName("emailsender.jsp");
                model.addObject("email", email);
                model.addObject("token", token);
                model.addObject("expiry", expiry);
                return model;
            } else if (result instanceof UserErrorModel) {
                log.warn("Invalid token for email: {}", email);
//                model.setViewName("invalidtoken.jsp");//we can do it
                model.addObject("message", " Invalid or tampered token.");
                return model;
            } else {
                log.error("Unexpected response from token check for email: {}", email);
//                model.setViewName("error.jsp");//we can do it
                model.addObject("message", "⚠ Unknown error while validating token.");
                return model;
            }

        } catch (Exception e) {
            log.error("Exception while checking token for email: {}, Error: {}", email, e.getMessage(), e);
//            model.setViewName("error.jsp");//we can do it
            model.addObject("message", " Internal server error occurred: " + e.getMessage());
            return model;
        }
    }

}
/*
*                //HttpHeaders headers = new HttpHeaders();
                 //headers.add("Location", "redirect:emailsender?email=" + email +"&token=" + token);
                 // return new ResponseEntity<String>(headers, HttpStatus.FOUND);
 *               //  return new ResponseEntity<String>("redirect:emailsender?email=" + email +"&token=" + token, HttpStatus.FOUND);

*
*
* */