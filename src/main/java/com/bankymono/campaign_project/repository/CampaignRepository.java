package com.bankymono.campaign_project.repository;

import com.bankymono.campaign_project.model.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
}
