package br.com.gabnest.nest_gab_api.dto.user;

import br.com.gabnest.nest_gab_api.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserSummary {
    private String id;
    private String name;
    private UserRole role;
}
