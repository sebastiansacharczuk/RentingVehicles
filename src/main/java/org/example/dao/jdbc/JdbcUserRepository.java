package org.example.dao.jdbc;

import org.example.dao.IUserRepository;
import org.example.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserRepository implements IUserRepository {
    private List<User> users;
    private final String filename;


    public JdbcUserRepository(String filename) {
        this.filename = filename;
        this.users = new ArrayList<>();
    }

    @Override
    public User getUser(int id) {
        for (User user : users) {
            if (user.id == id) {
                return user;
            }
        }
        return null;
    }

    @Override
    public List<User> getUsers() {
        return users;
    }

    @Override
    public void addUser(User user) {
        users.add(user);
        save();
    }

    @Override
    public boolean rentVehicle(int userId, int vehicleId) {
        for (User user : users) {
            if (user.id == userId) {
                user.setRentedId(vehicleId);
                save();
                return true;
            }
        }
        return false;
    }




    public void load() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");

                if (fields.length > 0) {
                    for (int i = 0; i < fields.length; i++) {
                        User newUser = new User(Integer.parseInt(fields[i]), fields[i+1], fields[i+2], Integer.parseInt(fields[i+3]));
                        users.add(newUser);
                        i+=6;
                    }
                }
            }
            if (users.isEmpty())
                User.idGen = 0;
            else
                User.idGen = users.getLast().id + 1;

            reader.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    @Override
    public void save() {
        try {
            StringBuilder usersStr = new StringBuilder();
            for (User user : users) {
                usersStr.append(user.toCSV() + '\n');
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(this.filename));
            writer.write(String.valueOf(usersStr));

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String clientProfileInfo(User user) {
        return String.format("ID: %d\nUsername: %s", user.id, user.getUsername());
    }
}
