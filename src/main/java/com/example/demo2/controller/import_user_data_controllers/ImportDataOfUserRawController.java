package com.example.demo2.controller.import_user_data_controllers;

import com.example.demo2.ServiceImpl.import_data_of_user_implements.import_raw_data_implements.ImportRawDataOfUserImplements;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserRequestModel;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserResponseModel;
import com.example.demo2.model.success.SuccessDetailsModel;
import com.example.demo2.model.userdetails.UserErrorModel;
import com.example.demo2.model_service.RawDataOfUserErrorSuccess;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/{version}/api/user/raw")
public class ImportDataOfUserRawController {

    @Autowired
    ImportRawDataOfUserImplements importRawDataOfUserImplements;

    @PostMapping("/raw")
    @Operation(summary = "raw new user data")
    public ResponseEntity<Object> createNewRawData(@RequestBody RawDataOfUserRequestModel rawDataOfUserRequestModel, @RequestHeader("Authorization") String authHeader) {

        String fileName = rawDataOfUserRequestModel.getFileName();
        String filePath = rawDataOfUserRequestModel.getFilePath();

        // Regex for validating file name (.csv or .xlsx)
        String regex = "^[a-zA-Z]{3,20000}.*[0-9]+.*\\.(csv|xlsx)$";
        Pattern pattern = Pattern.compile(regex);
        if (fileName == null) {
            log.warn("Missing required file name.");
            return new ResponseEntity<>("Please provide valid file name and file path.", HttpStatus.BAD_REQUEST);
        }
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            log.warn("Invalid file name format: {}", fileName);
            return new ResponseEntity<>("Invalid file name format. Allowed formats: .csv or .xlsx", HttpStatus.BAD_REQUEST);
        }
        try {
            RawDataOfUserErrorSuccess result = importRawDataOfUserImplements.rawNewDataOfUser(rawDataOfUserRequestModel, authHeader);

            if (result instanceof SuccessDetailsModel successDetails) {
                log.info("User data imported successfully: {} - {}", successDetails.getSuccess(), successDetails.getDetails());
                return new ResponseEntity<>(successDetails, HttpStatus.CREATED);
            } else if (result instanceof UserErrorModel userError) {
                log.warn("User data raw error: {} - {}", userError.getError(), userError.getDetails());
                return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST);
            } else {
                log.error("Unexpected error during raw: {}", result);
                return new ResponseEntity<>("An unexpected server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Exception during data raw: ", e);
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/raw/get")
    @Operation(summary = "for search data with fileName ")
    public ResponseEntity<Object> getRawDataByFileName(@RequestParam("fileName") String fileName) {
        log.info("Searching file with name: {}", fileName);
        String regex = "^[a-zA-Z]{3,20000}.*[0-9]+.*\\.(csv|xlsx)$";
        Pattern pattern = Pattern.compile(regex);
        if (fileName == null) {
            log.warn("Missing required file name.");
            return new ResponseEntity<>("Please provide valid file name and file path.", HttpStatus.BAD_REQUEST);
        }
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            log.warn("Invalid file name format: {}", fileName);
            return new ResponseEntity<>("Invalid file name format. Allowed formats: .csv or .xlsx", HttpStatus.BAD_REQUEST);
        }
        try {
            RawDataOfUserErrorSuccess result = importRawDataOfUserImplements.getRawDataByFileName(fileName);

            if (result != null) {
                log.info("File found, returning data.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                log.warn("File not found with name: {}", fileName);
                return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            log.error("Error while searching file: {}", e.getMessage(), e);
            return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/raw")
    @Operation(summary = "List all raw data")
    public ResponseEntity<Object> listAllRaw() {
        log.info("Searching files...");
        try {
            List<RawDataOfUserResponseModel> result = importRawDataOfUserImplements.listAllRawData();
            if (result != null && !result.isEmpty()) {
                log.info("Files found, returning data.");
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                log.warn("No files found.");
                return new ResponseEntity<>("No files found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error while searching files: {}", e.getMessage(), e);
            return new ResponseEntity<>("Server Error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
