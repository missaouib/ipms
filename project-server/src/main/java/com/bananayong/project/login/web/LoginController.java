package com.bananayong.project.login.web;

import java.time.Instant;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bananayong.project.login.LoginService;
import com.bananayong.project.user.UserService;
import com.bananayong.project.user.UsernameExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final LoginService loginService;

    @PostMapping(path = "/sign-up")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        var user = this.userService.createUser(request.getUsername(), request.getPassword());
        return SignUpResponse.of(user.getUsername(), Instant.now());
    }

    @PostMapping(path = "/login")
    public LoginResponse login(
        @RequestBody LoginRequest request,
        HttpServletRequest httpServletRequest
    ) {
        var username = request.getUsername();
        var password = request.getPassword();

        this.loginService.login(username, password);
        httpServletRequest.getSession(true);

        return LoginResponse.of(username, Instant.now());
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> usernameException(UsernameExistException ex) {
        var msg = Map.of("msg", "can not use username. username: " + ex.getUsername());
        return ResponseEntity.badRequest()
                             .body(msg);

    }
}
