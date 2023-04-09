package com.example.spring.service.user.controllers;

import com.example.spring.service.user.model.ColorLogger;
import com.example.spring.service.user.model.Database;
import com.example.spring.service.user.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Collectors;

@RestController
public class BaseController {
    private final Database database = new Database();
    ColorLogger colorLogger = new ColorLogger();

    @GetMapping("/users")
    public String getUsers() {
        colorLogger.logYellowInfo("Loaded all UsersDatabase");
        return "{users: [" + database.getUserBase().stream().map(User::toString).collect(Collectors.joining(", ")) + "] }";
    }

    @GetMapping("/users/{id}")
    public String getUser(@PathVariable("id") long id) {
        if (database.getUserBase().stream().noneMatch(existUser -> existUser.getId() == id)) {
            colorLogger.logRedInfo("Getting by wrong user ID");
            return "Cannot find user with this ID";
        }
        colorLogger.logYellowInfo("Loaded user with ID: " + id);
        return Objects.requireNonNull(database.getUserBase().stream().filter(user -> user.getId() == id).findAny().orElse(null)).toString();
    }

    @PostMapping("/users")
    public String addNewUser(@RequestBody User user) {
        if(user.getId() == 0){
            colorLogger.logRedInfo("Trying to create user with ID = 0");
            return "User ID cannot be equal to 0";
        }
        if (user.getName() == null){
            colorLogger.logRedInfo("Trying to create user with name equals null");
            return "User name cannot be equal to null";
        }
        if (database.getUserBase().stream().noneMatch(existUser -> existUser.getId() == user.getId())) {
            database.addUser(user);
            colorLogger.logGreenInfo("Created user with ID: " + user.getId() + " Name: " + user.getName());
            return "User added!";
        } else {
            colorLogger.logRedInfo("Failed to create user with ID: " + user.getId());
            return "User with this ID already exists";
        }
    }

    @PutMapping("/users/{id}")
    public String changeUser(@PathVariable("id") long id, @RequestBody User user) {
        if (user.getId() != 0) {
            if (database.getUserBase().stream().noneMatch(existUser -> existUser.getId() == user.getId())) {
                database.getUserBase().stream().filter(existUser -> existUser.getId() == id).forEach(existUser -> {
                    existUser.setId(user.getId());
                    if (user.getName() != null) {
                        existUser.setName(user.getName());
                    }
                });
            } else {
                colorLogger.logRedInfo("Failed to update user with ID: " + id);
                return "User with this id exists";
            }
        }
        if (user.getId() == 0) {
            database.getUserBase().stream().filter(existUser -> existUser.getId() == id).forEach(existUser -> existUser.setName(user.getName()));
        }
        colorLogger.logYellowInfo("User with ID: " + id + " updated -> new ID: " + user.getId() + " new name: " + user.getName());
        return "User has been updated";
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        database.getUserBase().removeIf(user -> id == user.getId());
        colorLogger.logGreenInfo("Deleted user with ID: " + id);
        return "User successfully deleted or never existed";
    }
}
