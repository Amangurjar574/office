package com.example.demo2.model;

import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserErrorModel implements UserErrorSuccess {
    String error;
    String details;
    String[] extra;
}
