package com.example.demo2.controller;

import com.example.demo2.ServiceImpl.ImportDataOfUserImplements;
import com.example.demo2.model.*;
import com.example.demo2.model_service.ImportDataOfUserErrorSuccess;
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
@RequestMapping("/{version}/api/user/import")
public class ImportDataOfUserController {

    @Autowired
    ImportDataOfUserImplements importDataOfUserImplements;

    @PostMapping("/newdata")
    @Operation(summary = "Import new user data")
    public ResponseEntity<Object> importNewData(@RequestBody ImportDataOfUserRequestModel importDataOfUserRequestModel,@RequestHeader("Authorization") String authHeader) {

        String fileName = importDataOfUserRequestModel.getFileName();
        String filePath = importDataOfUserRequestModel.getFilePath();

        // Regex for validating file name (.csv or .xlsx)
        String regex = "^[a-zA-Z]{3,20000}[0-9]+.*.(csv|xlsx)$";
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
            ImportDataOfUserErrorSuccess result = importDataOfUserImplements.importNewDataOfConvesion(importDataOfUserRequestModel,authHeader);

            if (result instanceof SuccessDetailsModel successDetails) {
                log.info("User data imported successfully: {} - {}", successDetails.getSuccess(), successDetails.getDetails());
                return new ResponseEntity<>(successDetails, HttpStatus.CREATED);
            } else if (result instanceof UserErrorModel userError) {
                log.warn("User data import error: {} - {}", userError.getError(), userError.getDetails());
                return new ResponseEntity<>(userError, HttpStatus.BAD_REQUEST);
            }

            else {
                log.error("Unexpected error during import: {}", result);
                return new ResponseEntity<>("An unexpected server error occurred.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            log.error("Exception during data import: ", e);
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/search/conversioncata/byfilename")
    @Operation(summary = "for search data with fileName ")
    public ResponseEntity<Object> searchConversionDataByFileName(@RequestParam("fileName") String fileName) {
        log.info("Searching file with name: {}", fileName);
        String regex = "^[a-zA-Z]{3,20000}[0-9]+.*.(csv|xlsx)$";
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
            ImportDataOfUserErrorSuccess result = importDataOfUserImplements.searchConversionDataByFileName(fileName);

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
    @GetMapping("/list/all/conversion/data")
    @Operation(summary = "List all conversion data")
    public ResponseEntity<Object> listAllConversionData() {
        log.info("Searching files...");
        try {
            List<ImportDataOfUserResponseModel> result = importDataOfUserImplements.listAllConversionData();
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
