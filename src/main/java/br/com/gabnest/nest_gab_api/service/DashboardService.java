package br.com.gabnest.nest_gab_api.service;

import br.com.gabnest.nest_gab_api.dto.project.ProjectSummary;
import br.com.gabnest.nest_gab_api.model.Idea;
import br.com.gabnest.nest_gab_api.model.Project;
import br.com.gabnest.nest_gab_api.model.enums.IdeaStatus;
import br.com.gabnest.nest_gab_api.model.enums.ProjectStatus;
import br.com.gabnest.nest_gab_api.repository.ProjectRepository;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProjectRepository projectRepository;
    private final MongoTemplate mongoTemplate;

    public DashboardResponse getDashboard() {
        var metrics = loadCompletedProjectMetrics();

        BigDecimal totalInvestmentCompleted = defaultDecimal(metrics.getTotalInvestment());
        BigDecimal totalActualReturn = defaultDecimal(metrics.getTotalActualReturn());

        BigDecimal roi = BigDecimal.ZERO;
        if (totalInvestmentCompleted.compareTo(BigDecimal.ZERO) > 0) {
            roi = totalActualReturn.subtract(totalInvestmentCompleted)
                    .divide(totalInvestmentCompleted, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        BigDecimal savings = totalActualReturn.subtract(totalInvestmentCompleted);

        long ideasImplemented = mongoTemplate.count(
                Query.query(Criteria.where("status").is(IdeaStatus.APPROVED.name())),
                Idea.class
        );

        List<ProjectSummary> summaries = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")).stream()
                .map(this::toProjectSummary)
                .toList();

        return DashboardResponse.builder()
                .totalRoi(roi)
                .totalSavings(savings.max(BigDecimal.ZERO))
                .completedProjects(defaultLong(metrics.getCompletedProjects()))
                .ideasImplemented(ideasImplemented)
                .projects(summaries)
                .build();
    }

    private DashboardMetrics loadCompletedProjectMetrics() {
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(Criteria.where("status").is(ProjectStatus.COMPLETED.name())),
                Aggregation.group()
                        .count().as("completedProjects")
                        .sum("investment").as("totalInvestment")
                        .sum("actualReturn").as("totalActualReturn")
        );

        AggregationResults<DashboardMetrics> results = mongoTemplate.aggregate(aggregation, Project.class, DashboardMetrics.class);
        DashboardMetrics metrics = results.getUniqueMappedResult();

        return metrics != null ? metrics : DashboardMetrics.builder().build();
    }

    private ProjectSummary toProjectSummary(Project project) {
        return ProjectSummary.builder()
                .id(project.getId())
                .title(project.getTitle())
                .status(project.getStatus())
                .stage(project.getStage())
                .investment(project.getInvestment())
                .expectedReturn(project.getExpectedReturn())
                .actualReturn(project.getActualReturn())
                .productivityGain(project.getProductivityGain())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .build();
    }

    private BigDecimal defaultDecimal(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private Long defaultLong(Long value) {
        return value != null ? value : 0L;
    }

    @Data
    @Builder
    private static class DashboardMetrics {
        @Builder.Default
        private Long completedProjects = 0L;
        @Builder.Default
        private BigDecimal totalInvestment = BigDecimal.ZERO;
        @Builder.Default
        private BigDecimal totalActualReturn = BigDecimal.ZERO;
    }

    @Data
    @Builder
    public static class DashboardResponse {
        private BigDecimal totalRoi;
        private BigDecimal totalSavings;
        private Long completedProjects;
        private Long ideasImplemented;
        private List<ProjectSummary> projects;
    }
}