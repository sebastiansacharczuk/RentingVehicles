package org.example.authenticate;

import org.example.dao.jdbc.JdbcUserRepository;
import org.example.model.User;

import java.security.MessageDigest;
import java.util.Objects;

public class Authenticator {
    public static User checkpass(String username, String password, JdbcUserRepository userRepository) {
        String encoded = hashString(password);
        for (User user : userRepository.getUsers()) {
            if (Objects.equals(user.getUsername(), username) && Objects.equals(user.getPassword(), encoded)) {
                return user;
            }
        }

        return null;
    }

    public static String hashString(String originalString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(originalString.getBytes());

            // Convert byte array to hexadecimal format
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
