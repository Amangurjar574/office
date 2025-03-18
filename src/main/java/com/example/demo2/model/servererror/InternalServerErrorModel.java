package com.example.demo2.model.servererror;

import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternalServerErrorModel implements UserErrorSuccess {
    String error;
}