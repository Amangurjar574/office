package com.example.demo2.ServiceImpl;

import com.example.demo2.Service.ImportDataOfUserService;
import com.example.demo2.entity.ImportDataOfUser;
import com.example.demo2.entity.User_Detail;
import com.example.demo2.jwt_security.JWTAuthenticationFilter;
import com.example.demo2.jwt_security.JwtHelper;
import com.example.demo2.model.*;
import com.example.demo2.model_service.ImportDataOfUserErrorSuccess;
import com.example.demo2.repo.ImportDataOfUserRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ImportDataOfUserImplements implements ImportDataOfUserService {
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    JWTAuthenticationFilter JWTAuthenticationFilter;

    @Autowired
    UserServiceImplements userServiceImplements;
    @Autowired
    ImportDataOfUserRepo importDataOfUserRepo;

    @Override
    public ImportDataOfUserErrorSuccess importNewDataOfConvesion(ImportDataOfUserRequestModel importDataOfUserRequestModel,String authHeader) {
        log.info("===> Starting importNewDataOfConvesion...");

        String email = jwtHelper.getUsernameFromToken(authHeader.substring(7));//pass_token
        log.info("Extracted email from token: {}", email);

        User_Detail userDetail = userServiceImplements.userRepo.findByEmail(email);
        ImportDataOfUser oldData =importDataOfUserRepo.findByFileName(importDataOfUserRequestModel.getFileName());
        if (isUserExists(oldData)) {
            log.warn("file Name already exists. Please use a different file Name: {}", importDataOfUserRequestModel.getFileName());
            return new UserErrorModel("file Name already exists. Please use a different file Name.",importDataOfUserRequestModel.getFileName(),null);
        }
        if (!userServiceImplements.isUserExists(userDetail)) {
            log.warn("User not found or token invalid for email: {}", email);
            return new UserDataNotFoundModel("User not found, token invalid.");
        }
        ImportDataOfUser newImportData = new ImportDataOfUser();
        newImportData.setUserDetail(userDetail);
        newImportData.setCreatedDate(LocalDateTime.now());
        newImportData.setFileName(importDataOfUserRequestModel.getFileName());
        log.info("Saving import data for file: {}", newImportData.getFileName());
        ImportDataOfUser savedData = importDataOfUserRepo.save(newImportData);

        if (savedData != null && savedData.getImportDataId() > 0) {
            log.info("Import data saved successfully for email: {}", email);
            return new SuccessDetailsModel("User created successfully", email);
        } else {
            log.error("Failed to save import data for user: {}", email);
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
    public ImportDataOfUserErrorSuccess searchConversionDataByFileName(String fileName) {
        log.info("Searching conversion data for file: {}", fileName);
        ImportDataOfUser importDataOfUserResult = importDataOfUserRepo.findByFileName(fileName);
        if (importDataOfUserResult != null) {
            ImportDataOfUserResponseModel ImportDataOfUserResponseModel = new ImportDataOfUserResponseModel(
                    fileName,
                    importDataOfUserResult.getDataCount(),
                    importDataOfUserResult.getUserDetail().getName(),
                    importDataOfUserResult.getCreatedDate(),
                    "Data successfully found..");

            log.info("Data found for file: {}, total records: {}", fileName);
            return ImportDataOfUserResponseModel;

        } else {
            log.warn("No data found for file: {}", fileName);
            return new UserErrorModel("File not found..", fileName, null);
        }
    }

    @Override
    public List<ImportDataOfUserResponseModel> listAllConversionData() {
        log.info("Fetching all conversion data from database...");

        List<ImportDataOfUser> result = importDataOfUserRepo.findAll();
        List<ImportDataOfUserResponseModel> newList = new ArrayList<>();

        if (result.isEmpty()) {
            log.warn("No conversion data found in database.");
        } else {
            for (ImportDataOfUser c : result) {
                ImportDataOfUserResponseModel responseModel = new ImportDataOfUserResponseModel(
                        c.getFileName(),
                        c.getDataCount(),
                        c.getUserDetail().getName(),
                        c.getCreatedDate(),
                        "Data successfully found."
                );
                newList.add(responseModel);
            }
            log.info("Total records fetched: {}", newList.size());
        }

        return newList;
    }


    public boolean isUserExists(ImportDataOfUser user) {
        return Optional.ofNullable(user).isPresent();
    }
}
