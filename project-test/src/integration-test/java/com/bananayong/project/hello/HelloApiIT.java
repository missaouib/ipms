package com.bananayong.project.hello;

import com.bananayong.project.AbstractITSupport;
import com.bananayong.project.api.HelloApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloApiIT extends AbstractITSupport {

    private HelloApi helloApi;

    @BeforeEach
    void setUp() {
        helloApi = retrofit.create(HelloApi.class);
    }

    @Test
    void testHelloApi() {
        // given
        var name = "Star";

        // when
        var message = helloApi.sayHello(name).blockingSingle();

        // then
        assertThat(message).isEqualTo("Hello Star.");
    }

}