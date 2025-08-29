package com.bankymono.campaign_project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public String storeFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(uploadPath); // Create directory if it doesn't exist

        String originalFileName = Objects.requireNonNull(file.getOriginalFilename());
        // Sanitize file name (optional but recommended for production)
        // String fileName = StringUtils.cleanPath(originalFileName);

        // Generate a unique file name to prevent overwriting and for security
        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;

        Path targetLocation = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), targetLocation);

        return uniqueFileName; // Return the unique file name for storage in DB
    }

    public Path loadFile(String fileName) {
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        return uploadPath.resolve(fileName).normalize();
    }
}
