package org.example;

import java.util.List;

public interface IUserRepository {
    User getUser(int id);
    List<User> getUsers();

    void addUser(User user);

    public boolean rentVehicle(int userId, int vehicleId);
    void save();

    void load();
}
