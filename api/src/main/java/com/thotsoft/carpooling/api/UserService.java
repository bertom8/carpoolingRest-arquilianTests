package com.thotsoft.carpooling.api;

import com.thotsoft.carpooling.api.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    boolean removeUser(int id);

    boolean removeUser(User user);

    User getUser(int id);

    void updateUser(int id, User user);

    List<User> listUsers();

    boolean isAlreadyRegistered(String email);
}
