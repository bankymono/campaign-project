package com.bankymono.campaign_project.controller;

import com.bankymono.campaign_project.model.AdCreative;
import com.bankymono.campaign_project.repository.AdCreativeRepository;
import com.bankymono.campaign_project.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/creatives")
public class AdCreativeController {
    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private AdCreativeRepository adCreativeRepository;

    @PostMapping("/upload")
    public ResponseEntity<AdCreative> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileStorageService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/creatives/download/")
                    .path(fileName)
                    .toUriString();

            AdCreative adCreative = new AdCreative(
                    fileName,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getSize(),
                    fileStorageService.loadFile(fileName).toString() // Store absolute path for clarity
            );
            adCreativeRepository.save(adCreative);

            return new ResponseEntity<>(adCreative, HttpStatus.CREATED);
        } catch (IOException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store file " + file.getOriginalFilename() + ". Please try again!", ex);
        }
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        try {
            Path filePath = fileStorageService.loadFile(fileName);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                // Try to determine file's content type
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream"; // Fallback if type cannot be determined
                }

                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(contentType))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "File not found " + fileName, ex);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading file " + fileName, e);
        }
    }

    @GetMapping
    public List<AdCreative> getAllCreatives() {
        return adCreativeRepository.findAll();
    }
}
