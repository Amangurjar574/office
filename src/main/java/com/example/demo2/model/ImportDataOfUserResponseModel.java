package com.example.demo2.model;

import com.example.demo2.model_service.ImportDataOfUserErrorSuccess;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportDataOfUserResponseModel implements ImportDataOfUserErrorSuccess {
    String fileName;
    long count;
    String name;
    LocalDateTime UploadedDateTime;
    String result;
}
