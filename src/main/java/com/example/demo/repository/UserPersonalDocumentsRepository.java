package com.example.demo.repository;

import com.example.demo.entity.UserPersonalDocuments;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserPersonalDocumentsRepository extends MongoRepository<UserPersonalDocuments, String> {
    Optional<UserPersonalDocuments> findByUserId(UUID userId);
}
