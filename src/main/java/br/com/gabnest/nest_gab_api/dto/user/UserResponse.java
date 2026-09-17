package br.com.gabnest.nest_gab_api.dto.user;

import br.com.gabnest.nest_gab_api.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String email;
    private UserRole role;
    private Boolean active;
    private LocalDateTime createdAt;
}
