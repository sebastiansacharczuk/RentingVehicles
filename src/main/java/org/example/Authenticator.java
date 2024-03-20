package org.example;

import java.util.Objects;

public class Authenticator {
    static User checkpass(String username, String password, IUserRepositoryImpl userRepository) {
        for (User user : userRepository.getUsers()) {
            if (Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), password)) {
                return user;
            }
        }

        return null;
    }
}
