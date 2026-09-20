package br.com.gabnest.nest_gab_api.service;

import br.com.gabnest.nest_gab_api.dto.idea.IdeaRequest;
import br.com.gabnest.nest_gab_api.dto.idea.IdeaReviewRequest;
import br.com.gabnest.nest_gab_api.model.Idea;
import br.com.gabnest.nest_gab_api.model.User;
import br.com.gabnest.nest_gab_api.model.enums.IdeaStatus;
import br.com.gabnest.nest_gab_api.model.enums.UserRole;
import br.com.gabnest.nest_gab_api.repository.IdeaRepository;
import br.com.gabnest.nest_gab_api.repository.StrategicGuidelineRepository;
import br.com.gabnest.nest_gab_api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IdeaServiceTest {

    @Mock
    private IdeaRepository ideaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StrategicGuidelineRepository guidelineRepository;

    @InjectMocks
    private IdeaService ideaService;

    @Test
    void shouldRejectUpdateFromAnotherOperator() {
        Idea idea = Idea.builder()
                .id("idea-1")
                .submittedById("operator-1")
                .status(IdeaStatus.PENDING)
                .build();
        IdeaRequest request = new IdeaRequest();

        when(ideaRepository.findById("idea-1")).thenReturn(Optional.of(idea));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> ideaService.update("idea-1", request, "operator-2")
        );

        assertEquals(403, exception.getStatusCode().value());
    }

    @Test
    void shouldRejectInvalidReviewTransition() {
        Idea idea = Idea.builder()
                .id("idea-1")
                .submittedById("operator-1")
                .status(IdeaStatus.APPROVED)
                .build();
        User reviewer = User.builder()
                .id("manager-1")
                .role(UserRole.MANAGER)
                .build();
        IdeaReviewRequest request = new IdeaReviewRequest();
        request.setStatus(IdeaStatus.REJECTED);

        when(ideaRepository.findById("idea-1")).thenReturn(Optional.of(idea));
        when(userRepository.findById("manager-1")).thenReturn(Optional.of(reviewer));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> ideaService.review("idea-1", request, "manager-1")
        );

        assertEquals(409, exception.getStatusCode().value());
    }
}