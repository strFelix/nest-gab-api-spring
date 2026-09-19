package br.com.gabnest.nest_gab_api.dto.guideline;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GuidelineHistoryResponse {
    private String id;
    private String guidelineId;
    private LocalDateTime date;
    private String category;
    private String campaign;
    private String contentSnapshot;
}

