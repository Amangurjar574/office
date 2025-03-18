package com.example.demo2.Service.importdataofuser.import_conversion_data_services;

import com.example.demo2.model.importdataofuser.import_conversion_data_of_user.ConversionDataOfUserRequestModel;
import com.example.demo2.model.importdataofuser.import_conversion_data_of_user.ConversionDataOfUserResponseModel;
import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;

import java.util.List;

public interface ImportDataOfConversionService {
    ConversionDataOfUserErrorSuccess createNewConversionData(ConversionDataOfUserRequestModel conversionDataOfUserRequestModel,String token);
    List<ConversionDataOfUserResponseModel> listAllConversion();
    ConversionDataOfUserErrorSuccess getByConversionId(long conversionId);
    ConversionDataOfUserErrorSuccess getByConversionFileName(String fileName);
    ConversionDataOfUserErrorSuccess deleteByConversionId(long conversionId);


}
