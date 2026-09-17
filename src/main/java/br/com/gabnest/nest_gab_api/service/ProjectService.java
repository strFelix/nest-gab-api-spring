package br.com.gabnest.nest_gab_api.service;

import br.com.gabnest.nest_gab_api.dto.idea.IdeaResponse;
import br.com.gabnest.nest_gab_api.dto.project.ProjectRequest;
import br.com.gabnest.nest_gab_api.dto.project.ProjectResponse;
import br.com.gabnest.nest_gab_api.dto.project.ProjectSummary;
import br.com.gabnest.nest_gab_api.dto.user.UserSummary;
import br.com.gabnest.nest_gab_api.model.Idea;
import br.com.gabnest.nest_gab_api.model.Project;
import br.com.gabnest.nest_gab_api.model.User;
import br.com.gabnest.nest_gab_api.repository.IdeaRepository;
import br.com.gabnest.nest_gab_api.repository.ProjectRepository;
import br.com.gabnest.nest_gab_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IdeaRepository ideaRepository;
    private final IdeaService ideaService;

    public ProjectResponse create(ProjectRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String ideaId = request.getIdeaId();
        if (ideaId != null) {
            // ensure idea exists
            ideaRepository.findById(ideaId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Idea not found"));
        }

        Project project = Project.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .stage(request.getStage())
                .investment(request.getInvestment())
                .expectedReturn(request.getExpectedReturn())
                .actualReturn(request.getActualReturn())
                .productivityGain(request.getProductivityGain())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .createdById(user.getId())
                .ideaId(ideaId)
                .build();

        return toResponse(projectRepository.save(project));
    }

    public List<ProjectSummary> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public ProjectResponse findById(String id) {
        return toResponse(findOrThrow(id));
    }

    public ProjectResponse update(String id, ProjectRequest request) {
        Project project = findOrThrow(id);

        project.setTitle(request.getTitle());
        project.setDescription(request.getDescription());
        project.setStatus(request.getStatus());
        project.setStage(request.getStage());
        project.setInvestment(request.getInvestment());
        project.setExpectedReturn(request.getExpectedReturn());
        project.setActualReturn(request.getActualReturn());
        project.setProductivityGain(request.getProductivityGain());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        return toResponse(projectRepository.save(project));
    }

    private Project findOrThrow(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private UserSummary toUserSummary(User user) {
        if (user == null) return null;
        return UserSummary.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    private ProjectSummary toSummary(Project p) {
        return ProjectSummary.builder()
                .id(p.getId())
                .title(p.getTitle())
                .status(p.getStatus())
                .stage(p.getStage())
                .investment(p.getInvestment())
                .expectedReturn(p.getExpectedReturn())
                .actualReturn(p.getActualReturn())
                .productivityGain(p.getProductivityGain())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .build();
    }

    private ProjectResponse toResponse(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .description(p.getDescription())
                .status(p.getStatus())
                .stage(p.getStage())
                .investment(p.getInvestment())
                .expectedReturn(p.getExpectedReturn())
                .actualReturn(p.getActualReturn())
                .productivityGain(p.getProductivityGain())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .createdBy(userRepository.findById(p.getCreatedById()).map(this::toUserSummary).orElse(null))
                .idea(p.getIdeaId() != null ? ideaService.findById(p.getIdeaId()) : null)
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}