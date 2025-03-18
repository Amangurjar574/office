package com.example.demo2.model.userdetails;

import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;
import com.example.demo2.model_service.RawDataOfUserErrorSuccess;
import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserErrorModel implements UserErrorSuccess , RawDataOfUserErrorSuccess, ConversionDataOfUserErrorSuccess {
    String error;
    String details;
    String[] extra;
}
