package org.example;

import java.lang.reflect.Array;
import java.util.List;

public class User {
    public String getUsername() {
        return username;
    }

    public int getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public User(String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role = role;
        id = idGen++;
    }

    public User(int id, String username, String password, int role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.id = id;
    }



    protected int id;
    private String username;
    private String password;
    private int role;
    private int rentedId = -1;

    protected static int idGen = 1;


    public String toCSV() {
        return String.format("%d;%s;%s;%d;%d;", id, username, password, role, rentedId);
    }

    public void setRentedId(int rentedId) {
        this.rentedId = rentedId;
    }
}
