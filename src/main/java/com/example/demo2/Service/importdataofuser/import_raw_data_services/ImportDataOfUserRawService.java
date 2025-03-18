package com.example.demo2.Service.importdataofuser.import_raw_data_services;

import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserRequestModel;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserResponseModel;
import com.example.demo2.model_service.RawDataOfUserErrorSuccess;

import java.util.List;

public interface ImportDataOfUserRawService {

    RawDataOfUserErrorSuccess rawNewDataOfUser(RawDataOfUserRequestModel rawDataOfUserRequestModel, String auth);
    RawDataOfUserErrorSuccess getRawDataByFileName(String fileName);
    List<RawDataOfUserResponseModel> listAllRawData();
}
