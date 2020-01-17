package com.bananayong.project.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import net.ttddyy.dsproxy.listener.logging.SLF4JLogLevel;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.util.StringUtils;

import static java.util.concurrent.TimeUnit.SECONDS;

@Profile("local")
@Configuration
public class ProxyDataSourceConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource dataSource(DataSourceProperties properties) {
        var hikariDataSource = properties.initializeDataSourceBuilder()
                                         .type(HikariDataSource.class)
                                         .build();
        if (StringUtils.hasText(properties.getName())) {
            hikariDataSource.setPoolName(properties.getName());
        }

        return ProxyDataSourceBuilder
            .create(hikariDataSource)
            .name("datasource-proxy")
            .autoRetrieveGeneratedKeys(true)
            .logQueryBySlf4j(SLF4JLogLevel.INFO)
            .logSlowQueryBySlf4j(3000, SECONDS, SLF4JLogLevel.INFO)
            .multiline()
            .build();
    }

}
