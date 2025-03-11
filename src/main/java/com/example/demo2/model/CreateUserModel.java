package com.example.demo2.model;

import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateUserModel implements UserErrorSuccess {
    String email;
    String name;
    String role;
}
