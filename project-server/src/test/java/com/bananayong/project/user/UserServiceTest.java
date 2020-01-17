package com.bananayong.project.user;

import com.bananayong.project.support.AbstractDatabaseTest;
import com.bananayong.project.support.Profiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles(Profiles.TEST)
@DataJpaTest
class UserServiceTest extends AbstractDatabaseTest {

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
    }

    @Test
    void testUserSave() {
        // given
        var username = "a";
        var password = "b"; // NOSONAR

        // when
        var user = userService.createUser(username, password);

        // then
        var savedUser = testEntityManager.find(User.class, user.getId());
        assertThat(user).isEqualTo(savedUser);
    }
}