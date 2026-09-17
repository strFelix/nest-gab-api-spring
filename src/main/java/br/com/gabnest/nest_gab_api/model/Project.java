package br.com.gabnest.nest_gab_api.model;

import br.com.gabnest.nest_gab_api.model.enums.ProjectStage;
import br.com.gabnest.nest_gab_api.model.enums.ProjectStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    private String id;

    private String title;

    private String description;

    @Field(targetType = FieldType.STRING)
    private ProjectStatus status = ProjectStatus.PLANNING;

    @Field(targetType = FieldType.STRING)
    private ProjectStage stage = ProjectStage.IDEATION;

    private BigDecimal investment;

    private BigDecimal expectedReturn;

    private BigDecimal actualReturn;

    private BigDecimal productivityGain;

    private LocalDate startDate;

    private LocalDate endDate;

    // Reference ids
    private String createdById;

    private String ideaId;

    // Link to guideline in effect (nullable)
    private String guidelineId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}