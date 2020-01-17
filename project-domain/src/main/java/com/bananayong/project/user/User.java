package com.bananayong.project.user;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString(exclude = "password")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String password;

    private Instant createdAt = Instant.now();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
