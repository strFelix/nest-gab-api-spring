package br.com.gabnest.nest_gab_api.controller;

import br.com.gabnest.nest_gab_api.dto.project.ProjectRequest;
import br.com.gabnest.nest_gab_api.dto.project.ProjectResponse;
import br.com.gabnest.nest_gab_api.dto.project.ProjectSummary;
import br.com.gabnest.nest_gab_api.service.JwtService;
import br.com.gabnest.nest_gab_api.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;
    private final JwtService jwtService;

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponse> create(
            @RequestBody @Valid ProjectRequest request,
            @RequestHeader("Authorization") String authHeader) {
        String userId = jwtService.extractUserId(authHeader.substring(7));
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request, userId));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'LEADER')")
    public ResponseEntity<List<ProjectSummary>> findAll() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/overview")
    @PreAuthorize("hasAnyRole('OPERATOR', 'MANAGER', 'LEADER')")
    public ResponseEntity<List<ProjectSummary>> overview() {
        return ResponseEntity.ok(projectService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'LEADER')")
    public ResponseEntity<ProjectResponse> findById(@PathVariable String id) {
        return ResponseEntity.ok(projectService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<ProjectResponse> update(
            @PathVariable String id,
            @RequestBody @Valid ProjectRequest request) {
        return ResponseEntity.ok(projectService.update(id, request));
    }
}