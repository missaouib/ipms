package com.bananayong.project.security;

import com.bananayong.project.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static org.springframework.security.crypto.factory.PasswordEncoderFactories.createDelegatingPasswordEncoder;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAuthenticationProvider implements AuthenticationProvider {

    private static final String[] USER_ROLES = {"ROLE_USER"};
    private final PasswordEncoder passwordEncoder = createDelegatingPasswordEncoder();
    private final UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        if (!supports(authentication.getClass())) {
            return null;
        }

        var token = (ApiAuthenticationToken) authentication;
        var username = token.getPrincipal();
        var presentedPassword = token.getCredentials();

        var user = this.userService.findUser(username)
                   .orElseThrow(this::notFoundUserException);

        if (!this.passwordEncoder.matches(presentedPassword, user.getPassword())) {
            log.debug("Authentication failed: password does not match stored value");
            throw badCredential();
        }

        return new ApiAuthenticationToken(user.getId(), user.getUsername(), USER_ROLES);
    }

    private BadCredentialsException badCredential() {
        return new BadCredentialsException("Bad credentials");
    }

    private BadCredentialsException notFoundUserException() {
        log.debug("Authentication failed: not found username");
        return badCredential();
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
