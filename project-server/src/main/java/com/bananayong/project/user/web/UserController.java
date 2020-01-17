package com.bananayong.project.user.web;

import com.bananayong.project.user.UserService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "users")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "{username}")
    public UserDTO getUser(@NotNull @PathVariable String username) {
        var user = this.userService.findUser(username);
        return user
            .map(UserDTO::toDTO)
            .orElseThrow(() -> new IllegalArgumentException("Not found username: " + username));
    }
}
