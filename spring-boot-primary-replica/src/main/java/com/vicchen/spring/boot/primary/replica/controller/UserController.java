package com.vicchen.spring.boot.primary.replica.controller;

import com.vicchen.spring.boot.primary.replica.entity.User;
import com.vicchen.spring.boot.primary.replica.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody String name) {
        log.info("🚀 [API] POST /api/users - Creating user: {}", name);
        User result = userService.createUser(name);
        log.info("✅ [API] POST /api/users - User created with ID: {}", result.getId());
        return result;
    }

    @GetMapping
    public List<User> list() {
        log.info("🚀 [API] GET /api/users - Fetching all users");
        List<User> result = userService.findAllUsers();
        log.info("✅ [API] GET /api/users - Found {} users", result.size());
        return result;
    }
}
