package com.bananayong.project.user.web;

import java.time.Instant;

import com.bananayong.project.user.User;
import lombok.Value;
import org.jetbrains.annotations.NotNull;

@Value
class UserDTO {
    Long id;
    String username;
    Instant createdAt;

    static UserDTO toDTO(@NotNull User user) {
        return new UserDTO(
            user.getId(),
            user.getUsername(),
            user.getCreatedAt()
        );
    }
}
