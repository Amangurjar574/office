package com.example.demo2.controller.import_user_data_controllers;

import com.example.demo2.ServiceImpl.import_data_of_user_implements.import_conversion_data_implements.ImportConversionDataOfUserImplements;
import com.example.demo2.model.importdataofuser.import_conversion_data_of_user.ConversionDataOfUserRequestModel;
import com.example.demo2.model.importdataofuser.import_conversion_data_of_user.ConversionDataOfUserResponseModel;
import com.example.demo2.model.importdataofuser.import_raw_data_of_user.RawDataOfUserResponseModel;
import com.example.demo2.model.success.SuccessDetailsModel;
import com.example.demo2.model.userdetails.UserErrorModel;
import com.example.demo2.model_service.ConversionDataOfUserErrorSuccess;
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
@RequestMapping("/{version}/api/conversion")
public class ImportDataOfUserConversionController {

    @Autowired
    ImportConversionDataOfUserImplements importConversionDataOfUserImplements;

    @PostMapping("/conversion")
    @Operation(summary = "Import user conversion data")
    public ResponseEntity<Object> createNewConversionData(@RequestBody ConversionDataOfUserRequestModel conversionDataOfUserRequestModel,@RequestHeader("Authorization") String token) {
        String fileName = conversionDataOfUserRequestModel.getFileName();
        String filePath = conversionDataOfUserRequestModel.getFilePath();

        // Regex for validating file name (.csv or .xlsx)
        String regex = "^[a-zA-Z]{3,20000}.*[0-9]+.*\\.(csv|xlsx)$";
        Pattern pattern = Pattern.compile(regex);

        if (fileName == null || filePath == null || fileName.isBlank() || filePath.isBlank()) {
            log.warn("Missing required file name or path.");
            return new ResponseEntity<>("Please provide valid file name and file path.", HttpStatus.BAD_REQUEST);
        }

        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.matches()) {
            log.warn("Invalid file name format: {}", fileName);
            return new ResponseEntity<>("Invalid file name format. Allowed formats: .csv or .xlsx", HttpStatus.BAD_REQUEST);
        }

        try {
            ConversionDataOfUserErrorSuccess result = importConversionDataOfUserImplements.createNewConversionData(conversionDataOfUserRequestModel,token);

            if (result instanceof SuccessDetailsModel successDetails) {
                log.info(" Conversion data imported successfully: {} - {}", successDetails.getSuccess(), successDetails.getDetails());
                return new ResponseEntity<>(result, HttpStatus.CREATED);
            } else if (result instanceof UserErrorModel userError) {
                log.warn(" Conversion data error: {} - {}", userError.getError(), userError.getDetails());
                return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST);
            } else {
                log.error("Unexpected result during conversion: {}", result);
                return new ResponseEntity<>("An unexpected server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Exception during conversion import: ", e);
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/conversion")
    public ResponseEntity<Object> listAllConversion() {
        log.info("Searching files...");
        try {
            List<ConversionDataOfUserResponseModel> result = importConversionDataOfUserImplements.listAllConversion();
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
    @GetMapping("/conversion/file/name")
    public ResponseEntity<Object> getByConversionFileName(@RequestParam String fileName) {
        log.info("Searching file with name: {}", fileName);
        String regex = "^[a-zA-Z]{3,20000}.*[0-9]+.*.(csv|xlsx)$";
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
            ConversionDataOfUserErrorSuccess result = importConversionDataOfUserImplements.getByConversionFileName(fileName);
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

    @DeleteMapping("/conversion/id")
    public ResponseEntity<Object> deleteByConversionId(@RequestParam long ConversionId) {
        return null;
    }
}

