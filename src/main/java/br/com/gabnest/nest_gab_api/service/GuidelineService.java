package br.com.gabnest.nest_gab_api.service;

import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineRequest;
import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineResponse;
import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineHistoryResponse;
import br.com.gabnest.nest_gab_api.dto.user.UserSummary;
import br.com.gabnest.nest_gab_api.model.GuidelineHistory;
import br.com.gabnest.nest_gab_api.model.StrategicGuideline;
import br.com.gabnest.nest_gab_api.model.User;
import br.com.gabnest.nest_gab_api.repository.GuidelineHistoryRepository;
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
public class GuidelineService {

    private final StrategicGuidelineRepository guidelineRepository;
    private final UserRepository userRepository;
    private final GuidelineHistoryRepository historyRepository;

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
                .category(request.getCategory())
                .campaign(request.getCampaign())
                .createdById(user.getId())
                .build();

        StrategicGuideline saved = guidelineRepository.save(guideline);
        saveHistory(saved);
        return toResponse(saved);
    }

    public GuidelineResponse update(String id, GuidelineRequest request) {
        StrategicGuideline guideline = findOrThrow(id);
        guideline.setTitle(request.getTitle());
        guideline.setContent(request.getContent());
        guideline.setCategory(request.getCategory());
        guideline.setCampaign(request.getCampaign());
        StrategicGuideline saved = guidelineRepository.save(guideline);
        saveHistory(saved);
        return toResponse(saved);
    }

    public void delete(String id) {
        StrategicGuideline guideline = findOrThrow(id);
        guideline.setActive(false);
        StrategicGuideline saved = guidelineRepository.save(guideline);
        saveHistory(saved);
    }

    public List<GuidelineHistoryResponse> findHistory(String guidelineId) {
        // Validate guideline exists
        findOrThrow(guidelineId);
        return historyRepository.findByGuidelineIdOrderByDateDesc(guidelineId)
                .stream()
                .map(this::historyToResponse)
                .toList();
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
                .category(g.getCategory())
                .campaign(g.getCampaign())
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

    private void saveHistory(StrategicGuideline guideline) {
        GuidelineHistory history = GuidelineHistory.builder()
                .guidelineId(guideline.getId())
                .date(LocalDateTime.now())
                .category(guideline.getCategory())
                .campaign(guideline.getCampaign())
                .contentSnapshot(guideline.getContent())
                .build();
        historyRepository.save(history);
    }

    private GuidelineHistoryResponse historyToResponse(GuidelineHistory h) {
        return GuidelineHistoryResponse.builder()
                .id(h.getId())
                .guidelineId(h.getGuidelineId())
                .date(h.getDate())
                .category(h.getCategory())
                .campaign(h.getCampaign())
                .contentSnapshot(h.getContentSnapshot())
                .build();
    }
}