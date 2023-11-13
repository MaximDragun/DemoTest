package com.example.demo.controller;

import com.example.demo.service.UserPersonalDocumentsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/v1/investment/documents")
public class DocumentController {
    private final UserPersonalDocumentsService userPersonalDocumentsService;

    @GetMapping
    public ResponseEntity<String> createDocument() throws IOException {//эндпоинт по созданию объекта, замены плейсхолдеров, конверта в пдф и сохранению в монгу
        UUID uuid = UUID.randomUUID();
        userPersonalDocumentsService.create(uuid);
        return ResponseEntity.ok(uuid.toString());
    }

    @GetMapping("/{userId}")// эндпоинт по получению пдф файла
    public ResponseEntity<byte[]> getDocument1(@PathVariable("userId") String userId
    ) {
        return ResponseEntity
                .ok()
                .header("Content-Disposition", "inline; filename=" + userId.toLowerCase() + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(userPersonalDocumentsService.getDoc(userId));
    }

}