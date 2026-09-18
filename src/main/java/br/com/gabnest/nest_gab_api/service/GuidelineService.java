package br.com.gabnest.nest_gab_api.service;

import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineRequest;
import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineResponse;
import br.com.gabnest.nest_gab_api.dto.user.UserSummary;
import br.com.gabnest.nest_gab_api.model.StrategicGuideline;
import br.com.gabnest.nest_gab_api.model.User;
import br.com.gabnest.nest_gab_api.repository.StrategicGuidelineRepository;
import br.com.gabnest.nest_gab_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GuidelineService {

    private final StrategicGuidelineRepository guidelineRepository;
    private final UserRepository userRepository;

    public List<GuidelineResponse> findAllActive() {
        return guidelineRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GuidelineResponse findById(String id) {
        return toResponse(findOrThrow(id));
    }

    public GuidelineResponse create(GuidelineRequest request, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        StrategicGuideline guideline = StrategicGuideline.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .createdById(user.getId())
                .build();

        return toResponse(guidelineRepository.save(guideline));
    }

    public GuidelineResponse update(String id, GuidelineRequest request) {
        StrategicGuideline guideline = findOrThrow(id);
        guideline.setTitle(request.getTitle());
        guideline.setContent(request.getContent());
        return toResponse(guidelineRepository.save(guideline));
    }

    public void delete(String id) {
        StrategicGuideline guideline = findOrThrow(id);
        guideline.setActive(false);
        guidelineRepository.save(guideline);
    }

    private StrategicGuideline findOrThrow(String id) {
        return guidelineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guideline not found"));
    }

    private GuidelineResponse toResponse(StrategicGuideline g) {
        return GuidelineResponse.builder()
                .id(g.getId())
                .title(g.getTitle())
                .content(g.getContent())
                .active(g.getActive())
                .createdBy(userToSummary(g.getCreatedById()))
                .createdAt(g.getCreatedAt())
                .updatedAt(g.getUpdatedAt())
                .build();
    }

    private UserSummary userToSummary(String userId) {
        if (userId == null) return null;
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return null;
        return UserSummary.builder()
                .id(user.getId())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}