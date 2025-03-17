package com.example.demo2.model;

import com.example.demo2.model_service.ImportDataOfUserErrorSuccess;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImportDataOfUserRequestModel implements ImportDataOfUserErrorSuccess {
    String fileName;
    String filePath;
}
