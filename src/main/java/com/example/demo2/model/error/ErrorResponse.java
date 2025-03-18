package com.example.demo2.model.error;

import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public   class ErrorResponse implements UserErrorSuccess {
    private String message;
    private String details;
}