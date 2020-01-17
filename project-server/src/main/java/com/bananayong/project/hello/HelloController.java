package com.bananayong.project.hello;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.springframework.http.ResponseEntity.badRequest;

@Slf4j
@RestController
public class HelloController {

    @GetMapping(path = "/hello")
    public String hello(@NotNull @Valid NameParam nameParam) {
        var message = "Hello " + capitalize(nameParam.getName().strip()) + ".";
        return message.repeat(nameParam.repeat);
    }

    @ExceptionHandler
    public ResponseEntity<String> badRequestException(BindException e) {
        var fieldError = e.getFieldError();
        if (fieldError == null) {
            log.warn("Non field error", e);
            return badRequest().body("BadRequest. ");
        }

        return badRequest().body("BadRequest. wrong parameter: " + fieldError.getField());
    }

    @Data
    private static class NameParam {
        @Length(min = 2)
        String name;
        @Min(1)
        int repeat = 1;
    }
}
