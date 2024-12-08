package com.cpxHelper.myPatients.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.persistence.EntityManager;

@Configuration
public class JpaConfig {

    private final EntityManager entityManager;

    public JpaConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Bean
    public ApplicationRunner verifyJpaSetup() {
        return args -> {
            System.out.println("Hibernate JPA EntityManager is working: " + entityManager);
        };
    }
}
