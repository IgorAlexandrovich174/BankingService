package com.example.bankingservice.controller;

import com.example.bankingservice.requests.AddUserRequest;
import com.example.bankingservice.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service/users")
public class ServiceUserController {
    private final UserService userService;

    public ServiceUserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public void addUser(@RequestBody AddUserRequest addUserRequest) throws Throwable {
        userService.addUser(addUserRequest);
    }
}
