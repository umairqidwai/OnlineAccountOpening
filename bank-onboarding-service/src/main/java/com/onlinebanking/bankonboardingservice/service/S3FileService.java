package com.onlinebanking.bankonboardingservice.service;

import com.onlinebanking.bankonboardingservice.model.Document;
import com.onlinebanking.bankonboardingservice.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.List;

@Service
public class S3FileService {

    private final S3Presigner s3Presigner;
    private final DocumentRepository documentRepository;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${document.upload.max-file-size}")
    private long maxFileSize;

    @Value("${document.upload.allowed-types}")
    private List<String> allowedTypes;

    public S3FileService(S3Presigner s3Presigner, DocumentRepository documentRepository) {
        this.s3Presigner = s3Presigner;
        this.documentRepository = documentRepository;
    }

    public void validateFile(String contentType, long fileSize){
        // Type Check
        if(!allowedTypes.contains(contentType)){
            throw new IllegalArgumentException("Invalid File type! Allowed types are: " + allowedTypes);
        }
        // Size Check
        if(fileSize > maxFileSize){
            throw new IllegalArgumentException("File size exceeds the maximum limit of " + (maxFileSize / (1024 * 1024)) + "MB.");
        }

    }

    public String generatePreSignedUrlAndSaveMetadata(String fileName, String contentType, long fileSize){

        // validate file properties
        validateFile(contentType, fileSize);

        // save metadata to Database with PENDING/UPLOADED status
        Document document = new Document();
        document.setFileName(fileName);
        document.setContentType(contentType);
        document.setFileSize(fileSize);
        document.setFileKey(fileName);
        document.setStatus("PENDING_UPLOAD");

        documentRepository.save(document);

        // Generate S3 Pre-signed URL
        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(contentType)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(15))
                .putObjectRequest(objectRequest)
                .build();
        return s3Presigner.presignPutObject(presignRequest).url().toString();
    }
}
