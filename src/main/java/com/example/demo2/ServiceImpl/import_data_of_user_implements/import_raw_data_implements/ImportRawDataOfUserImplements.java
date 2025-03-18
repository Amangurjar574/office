package com.example.demo2.ServiceImpl.import_data_of_user_implements.import_raw_data_implements;

import com.example.demo2.Service.importdataofuser.import_raw_data_services.ImportDataOfUserRawService;

import com.example.demo2.ServiceImpl.userimplements.UserServiceImplements;
import com.example.demo2.entity.import_user_entitys.RawDataOfUser;
import com.example.demo2.entity.user_entitys.User_Detail;
import com.example.demo2.jwt_security.JWTAuthenticationFilter;
import com.example.demo2.jwt_security.JwtHelper;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserRequestModel;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserResponseModel;
import com.example.demo2.model.success.SuccessDetailsModel;
import com.example.demo2.model.userdetails.UserDataNotFoundModel;
import com.example.demo2.model.userdetails.UserErrorModel;
import com.example.demo2.model_service.RawDataOfUserErrorSuccess;
import com.example.demo2.repo.RawDataOfUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ImportRawDataOfUserImplements implements ImportDataOfUserRawService {
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    JWTAuthenticationFilter JWTAuthenticationFilter;

    @Autowired
    UserServiceImplements userServiceImplements;
    @Autowired
    RawDataOfUserRepo rawDataOfUserRepo;

    @Override
    public RawDataOfUserErrorSuccess rawNewDataOfUser(RawDataOfUserRequestModel rawDataOfUserRequestModel, String authHeader) {
        log.info("===> Starting rawNewDataOfConvesion...");

        String email = jwtHelper.getUsernameFromToken(authHeader.substring(7));//pass_token
        log.info("Extracted email from token: {}", email);

        User_Detail userDetail = userServiceImplements.userRepo.findByEmail(email);
        RawDataOfUser oldData = rawDataOfUserRepo.findByFileName(rawDataOfUserRequestModel.getFileName());
        if (isUserExists(oldData)) {
            log.warn("file Name already exists. Please use a different file Name: {}", rawDataOfUserRequestModel.getFileName());
            return new UserErrorModel("file Name already exists. Please use a different file Name.", rawDataOfUserRequestModel.getFileName(),null);
        }
        if (!userServiceImplements.isUserExists(userDetail)) {
            log.warn("User not found or token invalid for email: {}", email);
            return new UserDataNotFoundModel("User not found, token invalid.");
        }
        RawDataOfUser newRawData = new RawDataOfUser();
        newRawData.setUserDetail(userDetail);
        newRawData.setCreatedDate(LocalDateTime.now());
        newRawData.setFileName(rawDataOfUserRequestModel.getFileName());
        log.info("Saving raw data for file: {}", newRawData.getFileName());
        RawDataOfUser savedData = rawDataOfUserRepo.save(newRawData);

        if (savedData != null && savedData.getRawDataId() > 0) {
            log.info("Raw data saved successfully for email: {}", email);
            return new SuccessDetailsModel("User created successfully", email,null);
        } else {
            log.error("Failed to save raw data for user: {}", email);
            return new UserErrorModel("Error occurred while saving user.", email, null);
        }
    }
//    private long countOfCSVFile(String file_path_and_name) {
//        log.info("Counting lines in CSV file: {}", file_path_and_name);
//        try (BufferedReader reader = new BufferedReader(new FileReader(file_path_and_name))) {
//            int lines = 0;
//            String line;
//            while ((line = reader.readLine()) != null) {
//                lines++;
//            }
//            log.info("Total lines counted: {}", lines);
//            return lines;
//        } catch (IOException e) {
//            log.error("Error reading file: {}", file_path_and_name, e);
//            throw new RuntimeException("Failed to read file: " + file_path_and_name, e);
//        }
//    }

    @Override
    public RawDataOfUserErrorSuccess getRawDataByFileName(String fileName) {
        log.info("Searching raw data for file: {}", fileName);
        RawDataOfUser rawDataOfUserResult = rawDataOfUserRepo.findByFileName(fileName);
        if (rawDataOfUserResult != null&&!rawDataOfUserResult.isDeleted()) {

            RawDataOfUserResponseModel RawDataOfUserResponseModel = new RawDataOfUserResponseModel(
                    rawDataOfUserResult.getRawDataId(),
                    rawDataOfUserResult.getUserDetail().getName(),
                    rawDataOfUserResult.getUserDetail().getEmail(),
                    rawDataOfUserResult.getUserDetail().getUserid(),
                    rawDataOfUserResult.getUpdatedDate(),
                    rawDataOfUserResult.getS3FilePath(),
                    rawDataOfUserResult.getStatus(),
                    rawDataOfUserResult.isDeleted(),
                    rawDataOfUserResult.getUserDetail().getRole(),
                    "Data successfully found..");

            log.info("Data found for file: {}, total records: {}", fileName);
            return RawDataOfUserResponseModel;

        } else {
            log.warn("No data found for file: {}", fileName);
            return new UserErrorModel("File not found..", fileName, null);
        }
    }

    @Override
    public List<RawDataOfUserResponseModel> listAllRawData() {
        log.info("Fetching all raw data from database...");

        List<RawDataOfUser> result = rawDataOfUserRepo.findAll();
        List<RawDataOfUserResponseModel> newList = new ArrayList<>();

        if (result.isEmpty()) {
            log.warn("No raw data found in database.");
        } else {
            for (RawDataOfUser c : result) {
                RawDataOfUserResponseModel responseModel = new RawDataOfUserResponseModel(
                       c.getRawDataId(),
                       c.getUserDetail().getName(),
                       c.getUserDetail().getEmail(),
                       c.getUserDetail().getUserid(),
                       c.getUpdatedDate(),
                       c.getS3FilePath(),
                       c.getStatus(),
                       c.isDeleted(),
                       c.getUserDetail().getRole(),
                        "Data successfully found..");
                newList.add(responseModel);
            }
            log.info("Total records fetched: {}", newList.size());
        }

        return newList;
    }


    public boolean isUserExists(RawDataOfUser user) {
        return Optional.ofNullable(user).isPresent();
    }
}
