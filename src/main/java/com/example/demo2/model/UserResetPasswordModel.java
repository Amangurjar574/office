package com.example.demo2.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResetPasswordModel {
    String email;
    String password;
}
