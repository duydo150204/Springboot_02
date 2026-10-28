package com.example.springboot_02.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// @RestController = a controller whose methods return data (text or JSON), not views.
@RestController
public class HelloController {

    private final String welcomeMessage;

    public HelloController(@Value("${app.welcome-message}") String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    // GET http://localhost:8080/welcome
    // "/" now redirects to Swagger UI (see config/WebConfig), so the welcome text lives here.
    // @GetMapping is like [HttpGet("/welcome")] on an action method.
    @GetMapping("/welcome")
    public String home() {
        return welcomeMessage;
    }

    // GET http://localhost:8080/hello?name=Duy
    @GetMapping("/hello")
    public String hello(@RequestParam(defaultValue = "World") String name) {
        return "Hello, " + name + "!";
    }

    // GET http://localhost:8080/hello/Duy
    @GetMapping("/hello/{name}")
    public String helloPath(@PathVariable String name) {
        return "Hello, " + name + " (from the path)!";
    }
}
