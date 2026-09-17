package br.com.gabnest.nest_gab_api.dto.guideline;

import br.com.gabnest.nest_gab_api.dto.user.UserSummary;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class GuidelineResponse {
    private String id;
    private String title;
    private String content;
    private Boolean active;
    private UserSummary createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
