package com.onlinebanking.bankonboardingservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name ="kyc_documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String contentType;
    private Long fileSize;
    private String fileKey;

    private LocalDateTime uploadedAt = LocalDateTime.now();
    private String status = "PENDING";
}
