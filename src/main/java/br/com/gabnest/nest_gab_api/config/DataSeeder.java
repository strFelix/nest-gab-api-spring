package br.com.gabnest.nest_gab_api.config;

import br.com.gabnest.nest_gab_api.model.User;
import br.com.gabnest.nest_gab_api.model.enums.UserRole;
import br.com.gabnest.nest_gab_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("!test")
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedUser("Carlos Operador", "carlos.operador@aguiabranca.com.br", UserRole.OPERATOR);
        seedUser("Ana Gestora", "ana.gestora@aguiabranca.com.br", UserRole.MANAGER);
        seedUser("Roberto Lider", "roberto.lider@aguiabranca.com.br", UserRole.LEADER);
    }

    private void seedUser(String name, String email, UserRole role) {
        if (userRepository.existsByEmail(email)) return;

        User user = User.builder()
                .name(name)
                .email(email)
                .password(passwordEncoder.encode("nest123"))
                .role(role)
                .build();

        userRepository.save(user);
        log.info("Seeded user {} <{}> role={}", name, email, role);
    }
}


