package ru.skypro.homework.config.audit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import ru.skypro.homework.model.User;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfiguration {
    @Bean
    AuditorAware<User> auditorProvider() {
        return new SpringSecurityAuditorAware();
    }
}
