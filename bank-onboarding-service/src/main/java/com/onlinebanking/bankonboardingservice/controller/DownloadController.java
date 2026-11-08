package com.onlinebanking.bankonboardingservice.controller;

import com.onlinebanking.bankonboardingservice.service.S3FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/documents")
public class DownloadController {

    private final S3FileService s3FileService;

    public DownloadController(S3FileService s3FileService) {
        this.s3FileService = s3FileService;
    }

    @GetMapping("/download-url/{fileName}")
    public ResponseEntity<?> getDownloadUrl(@PathVariable String fileName){
        String downloadUrl = s3FileService.generateDownloadPresignedUrl(fileName);
        return ResponseEntity.ok(Map.of(
            "message", "Download pre-signed URL generated successfully",
            "downloadUrl", downloadUrl
        ));
    }

}
