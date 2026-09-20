package br.com.gabnest.nest_gab_api.controller;

import br.com.gabnest.nest_gab_api.dto.auth.AuthResponse;
import br.com.gabnest.nest_gab_api.dto.user.CreateUserRequest;
import br.com.gabnest.nest_gab_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthService authService;

    @PostMapping
    @PreAuthorize("hasRole('LEADER')")
    public ResponseEntity<AuthResponse> create(@RequestBody @Valid CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.createByLeader(request));
    }
}