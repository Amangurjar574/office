package com.example.demo2.model.importdataofuser.import_raw_data_of_user;

import com.example.demo2.model_service.RawDataOfUserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RawDataOfUserRequestModel implements RawDataOfUserErrorSuccess {
    String fileName;
    String filePath;
}
