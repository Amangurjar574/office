package com.example.demo2.model.jwt;

import lombok.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {
    private String token;
    private String role;
    private long id;
    private String name;
    private String email;
}
