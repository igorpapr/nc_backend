package net.dreamfteam.quiznet.controller;

import net.dreamfteam.quiznet.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;


@RestController
public class LoginController {
    private User loggedUser;

    @GetMapping("/api/login")
    @ResponseBody
    public Optional<User> show() {
        return Optional.ofNullable(loggedUser);
    }

    @PostMapping("/api/login")
    public User login(@RequestBody User user) {
        loggedUser = user;
        return user;
    }

    @PostMapping("/api/register")
    public User register(@RequestBody User user) {
        return user;
    }
}
