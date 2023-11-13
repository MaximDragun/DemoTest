package com.example.demo.service;

import com.example.demo.entity.UserPersonalDocuments;
import com.example.demo.repository.UserPersonalDocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserPersonalDocumentsDB {
    private final UserPersonalDocumentsRepository userPersonalDocumentsRepository;

    @Transactional
    public void createEnt(UserPersonalDocuments userPersonalDocuments) {
        userPersonalDocumentsRepository.save(userPersonalDocuments);
    }

    @Transactional
    public Optional<UserPersonalDocuments> getEnt(UUID uuid) {
      return   userPersonalDocumentsRepository.findByUserId(uuid);
    }

}
