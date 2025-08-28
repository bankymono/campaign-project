package com.bankymono.campaign_project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdCreative {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String originalFileName;
    private String contentType; // e.g., "image/jpeg", "video/mp4"
    private Long size;
    private String filePath;
    private LocalDateTime uploadDate;

    // Relationship to Campaign (optional for now, but good to think ahead)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "campaign_id")
    // private Campaign campaign;
}
