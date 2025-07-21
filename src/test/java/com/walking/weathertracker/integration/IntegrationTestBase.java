package com.walking.weathertracker.integration;

import com.walking.weathertracker.integration.annotation.IT;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;


@IT
@Sql(scripts = "classpath:sql/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@WithMockUser(username = "test@gmail.com", password = "testPassword")
public abstract class IntegrationTestBase {
    private static final PostgreSQLContainer<?> container;

    static {
        container = new PostgreSQLContainer<>("postgres:17.2");
        container.start();
    }

    @DynamicPropertySource
    static void postgresProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}
