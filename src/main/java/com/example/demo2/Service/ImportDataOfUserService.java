package com.example.demo2.Service;

import com.example.demo2.entity.ImportDataOfUser;
import com.example.demo2.model.ImportDataOfUserRequestModel;
import com.example.demo2.model.ImportDataOfUserResponseModel;
import com.example.demo2.model_service.ImportDataOfUserErrorSuccess;

import java.util.List;

public interface ImportDataOfUserService {

    ImportDataOfUserErrorSuccess importNewDataOfConvesion(ImportDataOfUserRequestModel importDataOfUserRequestModel,String auth);
    ImportDataOfUserErrorSuccess searchConversionDataByFileName(String fileName);
    List<ImportDataOfUserResponseModel> listAllConversionData();
}
