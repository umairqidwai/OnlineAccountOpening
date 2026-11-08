package com.onlinebanking.bankonboardingservice.controller;

import com.onlinebanking.bankonboardingservice.model.DocumentRequest;
import com.onlinebanking.bankonboardingservice.service.FileService;
import com.onlinebanking.bankonboardingservice.service.S3FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class UploadController {

    @Autowired
    private FileService fileService;

    @Autowired
    private S3FileService s3FileService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file")MultipartFile file){
        try{
            fileService.save(file);
            return ResponseEntity.ok("File uploaded successfully to local folder!");
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("Failed: " + e.getMessage());
                            }
    }

    @PostMapping("/signed-url")
    public ResponseEntity<?> getSignedUrl(@RequestBody DocumentRequest request){

        String signedUrl = s3FileService.generatePreSignedUrlAndSaveMetadata(request.fileName(), request.contentType(), request.fileSize());

        return ResponseEntity.ok(Map.of("message", "Pre-signed URL generated successfully and record saved.",
                "uploadUrl", signedUrl));
    }
}
