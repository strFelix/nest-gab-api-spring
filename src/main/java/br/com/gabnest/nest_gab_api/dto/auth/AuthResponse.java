package br.com.gabnest.nest_gab_api.dto.auth;

import br.com.gabnest.nest_gab_api.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String userId;
    private String name;
    private UserRole role;
}