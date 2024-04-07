package org.example.dao;

import org.example.model.User;

import java.util.Collection;
import java.util.List;

public interface IUserRepository {
    User getUser(String username);
    Collection<User> getUsers();

    void addUser(User user);
    void removeUser(String login);

}
