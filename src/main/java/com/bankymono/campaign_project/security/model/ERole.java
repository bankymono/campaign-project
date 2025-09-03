package com.bankymono.campaign_project.security.model;

public enum ERole {
    ROLE_USER,      // Default client role
    ROLE_MODERATOR, // Could be for campaign approval or more privileged clients
    ROLE_ADMIN      // Full access
}
