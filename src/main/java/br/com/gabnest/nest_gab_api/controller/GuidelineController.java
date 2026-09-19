package br.com.gabnest.nest_gab_api.controller;

import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineRequest;
import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineResponse;
import br.com.gabnest.nest_gab_api.dto.guideline.GuidelineHistoryResponse;
import br.com.gabnest.nest_gab_api.service.GuidelineService;
import br.com.gabnest.nest_gab_api.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guidelines")
@RequiredArgsConstructor
public class GuidelineController {

    private final GuidelineService guidelineService;
    private final JwtService jwtService;

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER', 'LEADER')")
    public ResponseEntity<List<GuidelineResponse>> findAll() {
        return ResponseEntity.ok(guidelineService.findAllActive());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER', 'LEADER')")
    public ResponseEntity<GuidelineResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(guidelineService.findById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<GuidelineResponse> create(
            @RequestBody @Valid GuidelineRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.status(HttpStatus.CREATED).body(guidelineService.create(request, userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<GuidelineResponse> update(
            @PathVariable String id,
            @RequestBody @Valid GuidelineRequest request) {
        return ResponseEntity.ok(guidelineService.update(id, request));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<List<GuidelineHistoryResponse>> findHistory(@PathVariable String id) {
        return ResponseEntity.ok(guidelineService.findHistory(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        guidelineService.delete(id);
        return ResponseEntity.noContent().build();
    }
}