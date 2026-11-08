package com.onlinebanking.bankonboardingservice.repository;

import com.onlinebanking.bankonboardingservice.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}
