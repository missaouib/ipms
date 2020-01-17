package com.bananayong.project.config;

import com.bananayong.project.security.ApiAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@EnableWebSecurity
public class WebSecurityConfig { // NOSONAR

    @Configuration
    @RequiredArgsConstructor
    public class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final ApiAuthenticationProvider authenticationProvider;

        @Bean(name = "authenticationManager")
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) {
            auth.authenticationProvider(this.authenticationProvider);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                .authorizeRequests()
                    .mvcMatchers("/hello").permitAll()
                    .mvcMatchers("/sign-up", "login").permitAll()
                    .anyRequest().hasRole("USER")
                    .and()
                .httpBasic()
                    .disable()
                .formLogin()
                    .disable()
                .logout()
                    .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
                    .and()
                .csrf()
                    .disable()
                .sessionManagement()
                    .sessionCreationPolicy(NEVER);
            // @formatter:on
        }
    }

    @Order(1)
    @Configuration
    @RequiredArgsConstructor
    public class ActuatorSecurityConfig extends WebSecurityConfigurerAdapter {

        private static final String ACTUATOR_ROLE = "ADMIN";
        private final SecurityProperties securityProperties;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.requestMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeRequests()
                .requestMatchers(
                        EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)
                )
                    .permitAll()
                .anyRequest()
                    .hasRole(ACTUATOR_ROLE)
                    .and()
                .formLogin()
                    .and()
                .httpBasic();
            // @formatter:on
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            var user = this.securityProperties.getUser();
            auth
                .inMemoryAuthentication()
                    .withUser(user.getName())
                        .password(user.getPassword())
                        .roles(ACTUATOR_ROLE);
        }
    }
}
