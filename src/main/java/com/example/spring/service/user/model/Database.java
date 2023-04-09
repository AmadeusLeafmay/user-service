package com.example.spring.service.user.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Database {
    private final ArrayList<User> userBase = new ArrayList<>();
    public void addUser(User user){
        userBase.add(user);
    }
}
