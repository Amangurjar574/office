package com.example.demo2.ServiceImpl.import_data_of_user_implements.import_conversion_data_implements;

import com.example.demo2.Service.importdataofuser.import_conversion_data_services.ImportDataOfConversionService;
import com.example.demo2.ServiceImpl.userimplements.UserServiceImplements;
import com.example.demo2.entity.import_user_entitys.ConversionDataOfUser;
import com.example.demo2.entity.import_user_entitys.RawDataOfUser;
import com.example.demo2.entity.user_entitys.User_Detail;
import com.example.demo2.jwt_security.JWTAuthenticationFilter;
import com.example.demo2.jwt_security.JwtHelper;
import com.example.demo2.model.importdataofuser.import_conversion_data_of_user.ConversionDataOfUserRequestModel;
import com.example.demo2.model.importdataofuser.import_conversion_data_of_user.ConversionDataOfUserResponseModel;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserResponseModel;
import com.example.demo2.model.success.SuccessDetailsModel;
import com.example.demo2.model.userdetails.UserDataNotFoundModel;
import com.example.demo2.model.userdetails.UserErrorModel;
import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;
import com.example.demo2.repo.ConversionDataOfUserRepo;
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
public class ImportConversionDataOfUserImplements implements ImportDataOfConversionService {

    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    com.example.demo2.jwt_security.JWTAuthenticationFilter JWTAuthenticationFilter;

    @Autowired
    UserServiceImplements userServiceImplements;

    @Autowired
    ConversionDataOfUserRepo conversionDataOfUserRepo;

    @Override
    public ConversionDataOfUserErrorSuccess createNewConversionData(ConversionDataOfUserRequestModel conversionDataOfUserRequestModel, String token) {
        log.info("===> Starting rawNewDataOfConvesion...");
        String email = jwtHelper.getUsernameFromToken(token.substring(7));//pass_token
        log.info("Extracted email from token: {}", email);
        User_Detail userDetail = userServiceImplements.userRepo.findByEmail(email);
        ConversionDataOfUser oldData = conversionDataOfUserRepo.findByFileName(conversionDataOfUserRequestModel.getFileName());
        if (isUserExists(oldData)) {
            log.warn("file Name already exists. Please use a different file Name: {}", conversionDataOfUserRequestModel.getFileName());
            return new UserErrorModel("file Name already exists. Please use a different file Name.", conversionDataOfUserRequestModel.getFileName(), null);
        }
        if (!userServiceImplements.isUserExists(userDetail)) {
            log.warn("User not found or token invalid for email: {}", email);
            return new UserDataNotFoundModel("User not found, token invalid.");
        }
        ConversionDataOfUser ConversionDataOfUser = new ConversionDataOfUser();
        ConversionDataOfUser.setUserDetail(userDetail);
        ConversionDataOfUser.setCreatedDate(LocalDateTime.now());
        ConversionDataOfUser.setFileName(conversionDataOfUserRequestModel.getFileName());
        ConversionDataOfUser.setS3FilePath(conversionDataOfUserRequestModel.getFilePath());
        ConversionDataOfUser.setSegmentName(conversionDataOfUserRequestModel.getSegmentName());
        ConversionDataOfUser.setConversionType(conversionDataOfUserRequestModel.getConversionType());
        log.info("Saving Conversion data for file: {}", ConversionDataOfUser.getFileName());
        ConversionDataOfUser savedData = conversionDataOfUserRepo.save(ConversionDataOfUser);
        if (savedData != null && savedData.getConversionDataId() > 0) {
            log.info("Conversion data saved successfully for email: {}", email);
            return new SuccessDetailsModel("User created successfully", email, null);
        } else {
            log.error("Failed to save Conversion data for user: {}", email);
            return new UserErrorModel("Error occurred while saving user.", email, null);
        }
    }
    @Override
    public List<ConversionDataOfUserResponseModel> listAllConversion() {
        log.info("Fetching all conversion data from database...");

        List<ConversionDataOfUser> result = conversionDataOfUserRepo.findAll();
        List<ConversionDataOfUserResponseModel> newList = new ArrayList<>();
        if (result.isEmpty()) {
            log.warn("No conversion data found in database.");
        } else {
            for (ConversionDataOfUser c : result) {
                newList.add(createConversionDataOfUserResponseObject(c));
            }
            log.info("Total records fetched: {}", newList.size());
        }

        return newList;

    }

    @Override
    public ConversionDataOfUserErrorSuccess getByConversionId(long conversionId) {
        return null;
    }
    @Override
    public ConversionDataOfUserErrorSuccess getByConversionFileName(String fileName) {
        log.info("Searching conversion data for file: {}", fileName);
        ConversionDataOfUser conversionDataOfUser = conversionDataOfUserRepo.findByFileName(fileName);
        if (conversionDataOfUser != null) {
           log.info("Data found for file: {}, total records: {}", fileName);
            return createConversionDataOfUserResponseObject(conversionDataOfUser);

        } else {
            log.warn("No data found for file: {}", fileName);
            return new UserErrorModel("File not found..", fileName, null);
        }
    }

    private ConversionDataOfUserResponseModel createConversionDataOfUserResponseObject(ConversionDataOfUser conversionDataOfUser)
    {
        ConversionDataOfUserResponseModel conversionDataOfUserResponseModel = new ConversionDataOfUserResponseModel(
                conversionDataOfUser.getConversionDataId(),
                conversionDataOfUser.getFileName(),
                conversionDataOfUser.getSegmentName(),
                conversionDataOfUser.getUserDetail().getName(),
                conversionDataOfUser.getUserDetail().getEmail(),
                conversionDataOfUser.getDataCount(),
                conversionDataOfUser.getCreatedDate(),
                conversionDataOfUser.getS3Bucket(),
                conversionDataOfUser.getStatus(),
                conversionDataOfUser.isDeleted(),
                conversionDataOfUser.getUserDetail().getRole());
        return conversionDataOfUserResponseModel;
    }

    @Override
    public ConversionDataOfUserErrorSuccess deleteByConversionId(long conversionId) {
        return null;
    }

    public boolean isUserExists(ConversionDataOfUser user) {
        return Optional.ofNullable(user).isPresent();
    }
}


