package com.bananayong.project.login.web;

import java.time.Instant;

import lombok.Value;

@Value(staticConstructor = "of")
class LoginResponse {
    String username;
    Instant loginAt;
}
