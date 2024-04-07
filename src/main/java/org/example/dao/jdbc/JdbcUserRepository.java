package org.example.dao.jdbc;

import org.example.dao.IUserRepository;
import org.example.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JdbcUserRepository implements IUserRepository {

    private static JdbcUserRepository instance;
    private final DatabaseManager databaseManager;
    private static String GET_ALL_USERS_SQL = "SELECT login, password, role, rentedPlate FROM tuser";
    private static String GET_USER_SQL = "SELECT * FROM tuser WHERE login LIKE ?";
    private static String ADD_USER_SQL = "INSERT INTO tuser (login, password, rentedPlate, role) VALUES(?, ?, ?, ?)";
    private static String REMOVE_USER_SQL = "WITH deleted_user AS ( DELETE FROM tuser WHERE login = ? RETURNING rentedPlate ) UPDATE tvehicle SET rent = 0 WHERE plate IN (SELECT rentedPlate FROM deleted_user)";


    private JdbcUserRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }
    @Override
    public User getUser(String login) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try{
            connection = databaseManager.getConnection();
            connection.createStatement();
            preparedStatement = connection.prepareStatement(GET_USER_SQL);
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if(rs.next()) {
                user = new User(
                        rs.getString("login"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role")),
                        rs.getString("rentedPlate")
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return user;
    }
    @Override
    public Collection<User> getUsers() {
        Collection<User> users = new ArrayList<>();
        try(Connection connection = databaseManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(GET_ALL_USERS_SQL)) {
            while(resultSet.next()){
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                User.Role role = User.Role.valueOf(resultSet.getString("role"));
                String plate = resultSet.getString("rentedPlate");

                User user = new User(login, password, role,plate);
                users.add(user);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;
    }
    @Override
    public void addUser(User user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = databaseManager.getConnection();
            connection.createStatement();
            preparedStatement = connection.prepareStatement(ADD_USER_SQL);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getRentedPlate());
            preparedStatement.setString(4, user.getRole().toString());
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User added successfully.");
            }
            else {
                System.out.println("Failed to add user.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void removeUser(String login) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(REMOVE_USER_SQL);
            preparedStatement.setString(1, login);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User removed successfully.");
            } else {
                System.out.println("No user matching the login.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static JdbcUserRepository getInstance(){
        if (instance == null){
            instance = new JdbcUserRepository();
        }
        return instance;
    }
}
