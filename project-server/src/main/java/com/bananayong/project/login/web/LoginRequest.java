package com.bananayong.project.login.web;

import javax.validation.constraints.NotEmpty;

import lombok.Value;

@Value
class LoginRequest {
    @NotEmpty
    String username;
    @NotEmpty
    String password;
}
