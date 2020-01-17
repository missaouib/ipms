package com.bananayong.project;

import okhttp3.HttpUrl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
public abstract class AbstractITSupport { // NOSONAR

    @Container
    private static final MySQLContainer MYSQL;
    @Container
    private static final GenericContainer REDIS;

    @LocalServerPort
    public int port;

    protected Retrofit retrofit;

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

    @BeforeEach
    public void setupRetrofit() {
        retrofit = new Retrofit.Builder()
            .baseUrl(HttpUrl.get("http://localhost:" + port))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
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
