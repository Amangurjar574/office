package com.example.demo2.controller.jwt_controllers;

import com.example.demo2.entity.user_entitys.User_Detail;
import com.example.demo2.jwt_security.JwtHelper;
import com.example.demo2.model.error.ErrorResponse;
import com.example.demo2.model.jwt.JwtRequest;
import com.example.demo2.model.jwt.JwtResponse;
import com.example.demo2.repo.userRepo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class JwtAuthenticationController {
//    @Autowired
//    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    userRepo empRepo;
    @PostMapping("/login")
    @Operation(summary ="for login and generateToken")
    public ResponseEntity<Object> login(@RequestBody JwtRequest request) {
        try {
            // Authenticate user credentials
            doAuthenticate(request.getEmail(), request.getPassword());
            // Fetch user details from the repository
            User_Detail user = empRepo.findByEmail(request.getEmail());
            if (user == null) {
                // If the user is not found, return 404 (Not Found)
                ErrorResponse errorResponse = new ErrorResponse("User not found", "The user with the provided email does not exist.");
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404 Not Found
            }
            // Load user details for token generation
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = helper.generateToken(userDetails);

            // Return the response with the token and user details
            JwtResponse jwtResponse = new JwtResponse(token, user.getRole(), user.getUserid(), user.getName(), user.getEmail());
            return ResponseEntity.ok(jwtResponse); // 200 OK

        } catch (BadCredentialsException e) {
            // Handle invalid credentials, return 401 Unauthorized
            ErrorResponse errorResponse = new ErrorResponse("Invalid credentials", "The provided email or password is incorrect.");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED); // 401 Unauthorized
        } catch (Exception e) {
            // Handle other unexpected errors, return 500 Internal Server Error
            ErrorResponse errorResponse = new   ErrorResponse("An error occurred", "An unexpected error occurred while processing your request.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }

    private void doAuthenticate(String username, String password) {
      //It will check whether this user has passed authentication or not
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credentials Invalid !!");
        }

    }
}