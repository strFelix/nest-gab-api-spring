package br.com.gabnest.nest_gab_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Document(collection = "strategic_guidelines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StrategicGuideline {

    @Id
    private String id;

    private String title;

    private String content;

    private String category;

    private String campaign;

    @Builder.Default
    private Boolean active = true;

    private String createdById;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}