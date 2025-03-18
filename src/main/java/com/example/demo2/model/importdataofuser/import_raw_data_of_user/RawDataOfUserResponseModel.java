package com.example.demo2.model.importdataofuser.import_raw_data_of_user;

import com.example.demo2.model_service.RawDataOfUserErrorSuccess;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RawDataOfUserResponseModel implements RawDataOfUserErrorSuccess {
    long id;
    String name;
    String email;
    long count;
    LocalDateTime UploadedDateTime;
    String s3bucketpath;
    String status;
    boolean isDeleted;
    String role;
    String message;
}
