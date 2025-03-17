package com.example.demo2.model;

import com.example.demo2.model_service.ImportDataOfUserErrorSuccess;
import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessDetailsModel implements UserErrorSuccess  , ImportDataOfUserErrorSuccess {
    String success;
    String details;
}
