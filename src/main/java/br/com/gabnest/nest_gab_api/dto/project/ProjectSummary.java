package br.com.gabnest.nest_gab_api.dto.project;

import br.com.gabnest.nest_gab_api.model.enums.ProjectStage;
import br.com.gabnest.nest_gab_api.model.enums.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class ProjectSummary {
    private String id;
    private String title;
    private ProjectStatus status;
    private ProjectStage stage;
    private BigDecimal investment;
    private BigDecimal expectedReturn;
    private BigDecimal actualReturn;
    private BigDecimal productivityGain;
    private LocalDate startDate;
    private LocalDate endDate;
    private String ideaId;
    private String guidelineId;
}
