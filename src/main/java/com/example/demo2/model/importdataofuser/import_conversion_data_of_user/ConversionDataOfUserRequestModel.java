package com.example.demo2.model.importdataofuser.import_conversion_data_of_user;

import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;
import com.example.demo2.model_service.UserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConversionDataOfUserRequestModel implements ConversionDataOfUserErrorSuccess, UserErrorSuccess {
    String fileName;
    String segmentName;
    String conversionType;
    String filePath;
}
