package com.example.demo2.controller;

import com.example.demo2.ServiceImpl.UserServiceImplements;
import com.example.demo2.entity.User_Detail;
import com.example.demo2.model.*;
import com.example.demo2.model_service.UserErrorSuccess;
import com.sun.source.doctree.SummaryTree;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
@RequestMapping("/{version}/api/user")
public class usercontroller {

    @Autowired
    UserServiceImplements userServiceImplements;

    @DeleteMapping("/user/delete")
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

    @PutMapping("/user/edit")
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

    @GetMapping("/user/search")
    @Operation(summary ="for search: get By Id..")
    public ResponseEntity<Object> getByUserid(@Param("userid") long userid) {
        if (userid > 0) {
            User_Detail userDetail = userServiceImplements.userSearch(userid);
            if (userDetail != null) {
                log.info(userDetail.toString(),HttpStatus.OK);
                return new ResponseEntity<>(userDetail, HttpStatus.OK);// Return the user details with a 200 OK status
            } else {
                UserDataNotFoundModel userDataNotFoundModel=new UserDataNotFoundModel("Not found data. id:"+userid);
                log.warn(userDataNotFoundModel.getError(),HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(userDataNotFoundModel,HttpStatus.NOT_FOUND);// User not found, return 404 Not Found
            }
        } else {
            log.warn("Invalid user ID provided.",HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);// Invalid user ID provided, return 400 Bad Request
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
    @Operation(summary ="for check token valid or not ")
    public ModelAndView checkToken(@Param("email") String email, @Param("token") String token)
    {
        ModelAndView modal = new ModelAndView();
        UserErrorSuccess user_errorSuccess =userServiceImplements.checkToken(email,token);
            if(user_errorSuccess instanceof SuccessDetailsModel){
                modal.setViewName("emailsender.jsp?email="+email+"&token="+token);
                return modal;
            }
            else{
                return modal;
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