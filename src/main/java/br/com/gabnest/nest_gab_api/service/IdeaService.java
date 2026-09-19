package br.com.gabnest.nest_gab_api.service;

import br.com.gabnest.nest_gab_api.dto.idea.IdeaRequest;
import br.com.gabnest.nest_gab_api.dto.idea.IdeaResponse;
import br.com.gabnest.nest_gab_api.dto.idea.IdeaReviewRequest;
import br.com.gabnest.nest_gab_api.dto.user.UserSummary;
import br.com.gabnest.nest_gab_api.model.Idea;
import br.com.gabnest.nest_gab_api.model.StrategicGuideline;
import br.com.gabnest.nest_gab_api.model.User;
import br.com.gabnest.nest_gab_api.model.enums.IdeaStatus;
import br.com.gabnest.nest_gab_api.repository.IdeaRepository;
import br.com.gabnest.nest_gab_api.repository.StrategicGuidelineRepository;
import br.com.gabnest.nest_gab_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IdeaService {

    private final IdeaRepository ideaRepository;
    private final UserRepository userRepository;
    private final StrategicGuidelineRepository guidelineRepository;

    public IdeaResponse create(IdeaRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // Validate guidelineId if provided
        if (request.getGuidelineId() != null) {
            guidelineRepository.findById(request.getGuidelineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guideline not found"));
        }

        Idea idea = Idea.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(IdeaStatus.PENDING)
                .guidelineId(request.getGuidelineId())
                .submittedById(user.getId())
                .build();

        return toResponse(ideaRepository.save(idea));
    }

    public List<IdeaResponse> findAll() {
        return ideaRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<IdeaResponse> findByStatus(IdeaStatus status) {
        return ideaRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<IdeaResponse> findMyIdeas(String userId) {
        return ideaRepository.findBySubmittedById(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public IdeaResponse findById(String id) {
        return toResponse(findOrThrow(id));
    }

    public IdeaResponse review(String id, IdeaReviewRequest request, String reviewerId) {
        Idea idea = findOrThrow(id);

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        idea.setStatus(request.getStatus());
        idea.setPriority(request.getPriority());
        idea.setReviewedById(reviewer.getId());
        idea.setReviewedAt(LocalDateTime.now());

        return toResponse(ideaRepository.save(idea));
    }

    public IdeaResponse update(String id, IdeaRequest request, String userId) {
        Idea idea = findOrThrow(id);

        // Only owner can edit
        if (!idea.getSubmittedById().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only idea owner can edit");
        }

        // Only pending ideas can be edited
        if (!idea.getStatus().equals(IdeaStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending ideas can be edited");
        }

        // Validate guidelineId if provided
        if (request.getGuidelineId() != null) {
            guidelineRepository.findById(request.getGuidelineId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guideline not found"));
        }

        idea.setTitle(request.getTitle());
        idea.setDescription(request.getDescription());
        idea.setGuidelineId(request.getGuidelineId());

        return toResponse(ideaRepository.save(idea));
    }

    public void delete(String id, String userId) {
        Idea idea = findOrThrow(id);

        // Only owner can delete
        if (!idea.getSubmittedById().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only idea owner can delete");
        }

        // Only pending ideas can be deleted
        if (!idea.getStatus().equals(IdeaStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending ideas can be deleted");
        }

        ideaRepository.deleteById(id);
    }

    private Idea findOrThrow(String id) {
        return ideaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Idea not found"));
    }

    private UserSummary toUserSummary(User user) {
        if (user == null) return null;
        return UserSummary.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    private IdeaResponse toResponse(Idea idea) {

        UserSummary submittedBy = null;
        UserSummary reviewedBy = null;

        if (idea.getSubmittedById() != null) {
            submittedBy = userRepository.findById(idea.getSubmittedById())
                    .map(this::toUserSummary)
                    .orElse(null);
        }

        if (idea.getReviewedById() != null) {
            reviewedBy = userRepository.findById(idea.getReviewedById())
                    .map(this::toUserSummary)
                    .orElse(null);
        }

        return IdeaResponse.builder()
                .id(idea.getId())
                .title(idea.getTitle())
                .description(idea.getDescription())
                .status(idea.getStatus())
                .priority(idea.getPriority())
                .submittedBy(submittedBy)
                .reviewedBy(reviewedBy)
                .reviewedAt(idea.getReviewedAt())
                .guidelineId(idea.getGuidelineId())
                .createdAt(idea.getCreatedAt())
                .updatedAt(idea.getUpdatedAt())
                .build();
    }
}