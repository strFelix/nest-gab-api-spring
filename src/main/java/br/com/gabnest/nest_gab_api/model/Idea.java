package br.com.gabnest.nest_gab_api.model;

import br.com.gabnest.nest_gab_api.model.enums.IdeaStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "ideas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Idea {

    @Id
    private String id;

    private String title;

    private String description;

    @Field(targetType = FieldType.STRING)
    private IdeaStatus status = IdeaStatus.PENDING;

    private Integer priority;

    // References by id to avoid DBRefs and N+1; resolve user data in services when needed
    private String submittedById;

    private String reviewedById;

    private LocalDateTime reviewedAt;

    // Link to strategic guideline (nullable)
    private String guidelineId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}