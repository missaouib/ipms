package com.bananayong.project.support;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

@Testcontainers
public abstract class AbstractDatabaseTest { // NOSONAR

    @Container
    private static final MySQLContainer MYSQL;

    static {
        MYSQL = new MySQLContainer<>()
            .withCommand(
                "--default-authentication-plugin=mysql_native_password",
                "--general-log=true",
                "--slow-query-log=true"
            )
            .withTmpFs(Map.of("/var/lib/mysql", "rw"));
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
    }

}
