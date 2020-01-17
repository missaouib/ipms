package com.bananayong.project;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@ActiveProfiles("integration-test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Testcontainers
public abstract class AbstractIntegrationTestSupport { // NOSONAR

    @Container
    private static final MySQLContainer MYSQL;
    @Container
    private static final GenericContainer REDIS;

    static {
        MYSQL = new MySQLContainer<>()
            .withCommand(
                "--default-authentication-plugin=mysql_native_password",
                "--general-log=true",
                "--slow-query-log=true"
            )
            .withTmpFs(Map.of("/var/lib/mysql", "rw"));

        REDIS = new GenericContainer("redis").withExposedPorts(6379);
    }

    @BeforeAll
    public static void init() {
        var url = MYSQL.getJdbcUrl();
        var username = MYSQL.getUsername();
        var password = MYSQL.getPassword();

        System.setProperty("spring.datasource.url", url);
        System.setProperty("spring.datasource.username", username);
        System.setProperty("spring.datasource.password", password);

        System.setProperty("spring.flyway.url", url + "?useSSL=false");
        System.setProperty("spring.flyway.username", username);
        System.setProperty("spring.flyway.password", password);

        System.setProperty("spring.redis.port", "" + REDIS.getMappedPort(6379));
    }
}
