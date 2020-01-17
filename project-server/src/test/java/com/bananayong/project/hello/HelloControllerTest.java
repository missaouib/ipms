package com.bananayong.project.hello;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = HelloController.class)
@WithMockUser
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("test append hello message.")
    @Test
    void testGetHelloWithAppendHello() throws Exception {
        // given
        var name = "Mike";

        // when
        var action = mockMvc.perform(get("/hello?name={name}", name)); // NOSONAR

        // then
        var expected = "Hello Mike.";
        action
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }

    @DisplayName("test strip name.")
    @ParameterizedTest(name = "name is .{0}.")
    @ValueSource(strings = {" tom", "tom ", " tom "})
    void testGetHelloWithWhiteSpace(String name) throws Exception {
        // given

        // when
        var action = mockMvc.perform(get("/hello?name={name}", name));

        // then
        var expected = "Hello Tom.";
        action
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }

    @DisplayName("test capitalize name.")
    @Test
    void testGetHelloCapitalize() throws Exception {
        // given
        var name = "jack";

        // when
        var action = mockMvc.perform(get("/hello?name={name}", name));

        // then
        var expected = "Hello Jack.";
        action
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }

    @DisplayName("test repeat message.")
    @Test
    void testGetHelloRepeat() throws Exception {
        // given
        var name = "Joy";

        // when
        var action = mockMvc.perform(get("/hello?name={name}&repeat=2", name));

        // then
        var expected = "Hello Joy.Hello Joy.";
        action
            .andExpect(status().isOk())
            .andExpect(content().string(expected));
    }

    @DisplayName("test too short name.")
    @Test
    void testGetHelloWithBadname() throws Exception {
        // given
        var name = "y";

        // when
        var action = mockMvc.perform(get("/hello?name={name}", name));

        // then
        action
            .andExpect(status().isBadRequest())
            .andExpect(content().string("BadRequest. wrong parameter: name"));
    }
}