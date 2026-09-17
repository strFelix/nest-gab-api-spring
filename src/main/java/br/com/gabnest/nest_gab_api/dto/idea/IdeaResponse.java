package br.com.gabnest.nest_gab_api.dto.idea;

import br.com.gabnest.nest_gab_api.dto.user.UserSummary;
import br.com.gabnest.nest_gab_api.model.enums.IdeaStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class IdeaResponse {
    private String id;
    private String title;
    private String description;
    private IdeaStatus status;
    private Integer priority;
    private UserSummary submittedBy;
    private UserSummary reviewedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
