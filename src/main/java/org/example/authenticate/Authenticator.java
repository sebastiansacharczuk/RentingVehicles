package org.example.authenticate;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.dao.jdbc.JdbcUserRepository;
import org.example.model.User;

import java.security.MessageDigest;
import java.util.Objects;

public class Authenticator {
    public static User login(String username, String password) {
        JdbcUserRepository jdbcUserRepository = JdbcUserRepository.getInstance();
        User userFromDb = jdbcUserRepository.getUser(username);
        if ( userFromDb!= null && sha256Hex(password).equals(userFromDb.getPassword())) {
            return userFromDb;
        }
        return null;
    }


    public static String sha256Hex(String string){
        return DigestUtils.sha256Hex(string);
    }

}
