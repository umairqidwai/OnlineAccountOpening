package com.onlinebanking.bankonboardingservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileService {
    private final Path rootLocation = Paths.get("./uploaded-files");

    public FileService() {
        try{
            Files.createDirectories(rootLocation);
        }catch (IOException io){
            throw new RuntimeException("Could not initialize folder", io);
        }
    }

    public void save(MultipartFile file) throws IOException{
        Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
    }
}
