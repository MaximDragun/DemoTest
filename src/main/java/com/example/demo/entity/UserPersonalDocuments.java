package com.example.demo.entity;

import com.example.demo.service.TestEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "user_personal_documents")
public class UserPersonalDocuments {
    @Id
    private String id;

    @Field("userId")
    @Indexed(unique = true)
    private UUID userId;

    @Field("status_documents")
    private String statusUserDocuments;

    @Field("created_date_time")
    private LocalDateTime created;

    @Field("edit_date_time")
    private LocalDateTime lastEdited;

    @Field("documents")
    private List<UserDocument> userDocuments;
}
