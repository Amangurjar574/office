package com.example.demo2.model.success;

import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;
import com.example.demo2.model_service.RawDataOfUserErrorSuccess;
import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessDetailsModel implements UserErrorSuccess  , RawDataOfUserErrorSuccess, ConversionDataOfUserErrorSuccess {
    String success;
    String details;
    Object data ;
}
