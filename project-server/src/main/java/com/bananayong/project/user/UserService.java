package com.bananayong.project.user;

import java.util.Optional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Transactional
    public User createUser(String username, String password) {
        var encodedPassword = this.passwordEncoder.encode(password);
        try {
            return this.userRepository.saveAndFlush(new User(username, encodedPassword));
        }
        catch (DataIntegrityViolationException e) {
            log.info("User already exists. username: {}", username, e);
            throw new UsernameExistException(username, e);
        }
    }

    @Transactional(readOnly = true)
    public Optional<User> findUser(String username) {
        var user = this.userRepository.findByUsername(username);
        if (user != null) {
            return Optional.of(user);
        }

        return Optional.empty();
    }
}
