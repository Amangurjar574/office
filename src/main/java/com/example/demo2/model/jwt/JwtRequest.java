package com.example.demo2.model.jwt;

import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtRequest implements UserErrorSuccess {

    private String email;
    private String password;
}

