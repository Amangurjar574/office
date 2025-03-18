package com.example.demo2.model.userdetails;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEditDetailsModel {
    long userid;
    String name;
    String role;
}
