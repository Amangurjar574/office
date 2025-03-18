package com.example.demo2.model.importdataofuser.import_conversion_data_of_user;

import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;
import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionDataOfUserResponseModel  implements ConversionDataOfUserErrorSuccess, UserErrorSuccess {
    long ConversionDataId;
    String fileName;
    String segmentName;
    String userName;
    String userEmail;
    long dataCount;
    LocalDateTime createTime;
    String s3Bucket;
    String status;
    boolean isDeleted;
    String userRole;
}
