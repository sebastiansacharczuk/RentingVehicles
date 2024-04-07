package org.example;

import org.example.authenticate.Authenticator;
import org.example.dao.jdbc.JdbcUserRepository;
import org.example.model.User;

public class Main {
    public static void main(String[] args) {
//        JdbcUserRepository jdbcUserRepository = JdbcUserRepository.getInstance();
//        jdbcUserRepository.addUser(new User("client1", Authenticator.sha256Hex("client123"), User.Role.USER, null));
        App app = new App();
        app.run();
    }
}