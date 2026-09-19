package br.com.gabnest.nest_gab_api.controller;

import br.com.gabnest.nest_gab_api.dto.idea.IdeaRequest;
import br.com.gabnest.nest_gab_api.dto.idea.IdeaResponse;
import br.com.gabnest.nest_gab_api.dto.idea.IdeaReviewRequest;
import br.com.gabnest.nest_gab_api.model.enums.IdeaStatus;
import br.com.gabnest.nest_gab_api.service.IdeaService;
import br.com.gabnest.nest_gab_api.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ideas")
@RequiredArgsConstructor
public class IdeaController {

    private final IdeaService ideaService;
    private final JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<IdeaResponse> create(
            @RequestBody @Valid IdeaRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.status(HttpStatus.CREATED).body(ideaService.create(request, userId));
    }

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<IdeaResponse>> findAll(
            @RequestParam(required = false) IdeaStatus status) {
        if (status != null) {
            return ResponseEntity.ok(ideaService.findByStatus(status));
        }
        return ResponseEntity.ok(ideaService.findAll());
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<List<IdeaResponse>> findMyIdeas(
            @RequestHeader("Authorization") String authHeader) {
        String userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(ideaService.findMyIdeas(userId));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER')")
    public ResponseEntity<IdeaResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(ideaService.findById(id));
    }

    @PatchMapping("/{id}/review")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<IdeaResponse> review(
            @PathVariable String id,
            @RequestBody @Valid IdeaReviewRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String reviewerId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(ideaService.review(id, request, reviewerId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<IdeaResponse> update(
            @PathVariable String id,
            @RequestBody @Valid IdeaRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.ok(ideaService.update(id, request, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Void> delete(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        String userId = jwtService.extractUserId(authHeader.substring(7));
        ideaService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }
}