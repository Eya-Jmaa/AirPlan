package com.example.hotels.services;

import com.example.hotels.models.User;

import java.util.List;

public interface IService {
    void add(User user);
    void update(User user);
    void delete(int id);
    List<User> getAllUsers();
}
