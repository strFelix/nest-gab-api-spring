package br.com.gabnest.nest_gab_api.controller;

import br.com.gabnest.nest_gab_api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<DashboardService.DashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboard());
    }

    @GetMapping("/by-guideline")
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<List<DashboardService.DashboardGroupResponse>> getByGuideline() {
        return ResponseEntity.ok(dashboardService.getByGuideline());
    }

    @GetMapping("/by-project")
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<List<DashboardService.DashboardGroupResponse>> getByProject() {
        return ResponseEntity.ok(dashboardService.getByProject());
    }
}