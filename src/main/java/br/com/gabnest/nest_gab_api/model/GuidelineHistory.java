package br.com.gabnest.nest_gab_api.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "guideline_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuidelineHistory {

    @Id
    private String id;

    private String guidelineId;

    private LocalDateTime date;

    private String category;

    private String campaign;

    private String contentSnapshot;
}

