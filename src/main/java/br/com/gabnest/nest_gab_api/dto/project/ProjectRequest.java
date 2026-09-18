package br.com.gabnest.nest_gab_api.dto.project;

import br.com.gabnest.nest_gab_api.model.enums.ProjectStage;
import br.com.gabnest.nest_gab_api.model.enums.ProjectStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ProjectRequest {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 150, message = "Title must be between 3 and 150 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 4000, message = "Description must be between 10 and 4000 characters")
    private String description;

    @NotNull(message = "Status is required")
    private ProjectStatus status;

    @NotNull(message = "Stage is required")
    private ProjectStage stage;

    @NotNull(message = "Investment is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Investment must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Investment must have at most 13 integer digits and 2 decimal places")
    private BigDecimal investment;

    @NotNull(message = "Expected return is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Expected return must be greater than zero")
    @Digits(integer = 13, fraction = 2, message = "Expected return must have at most 13 integer digits and 2 decimal places")
    private BigDecimal expectedReturn;

    @DecimalMin(value = "0.0", message = "Actual return must be zero or greater")
    @Digits(integer = 13, fraction = 2, message = "Actual return must have at most 13 integer digits and 2 decimal places")
    private BigDecimal actualReturn;

    @DecimalMin(value = "0.0", message = "Productivity gain must be zero or greater")
    @DecimalMax(value = "100.0", message = "Productivity gain must not exceed 100%")
    @Digits(integer = 3, fraction = 2, message = "Productivity gain must have at most 3 integer digits and 2 decimal places")
    private BigDecimal productivityGain;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    private LocalDate endDate;

    private String ideaId;
}
