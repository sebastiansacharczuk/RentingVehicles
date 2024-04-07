package org.example.dao.jdbc;

import org.example.dao.IVehicleRepository;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcVehicleRepository implements IVehicleRepository {
    private static JdbcVehicleRepository instance;
    private final DatabaseManager databaseManager;
    private String GET_ALL_VEHICLE_SQL = "SELECT * FROM tvehicle";

    private String GET_VEHICLE_SQL = "SELECT * FROM tvehicle WHERE plate LIKE ?";
    private String RENT_VEHICLE_SQL = "UPDATE tvehicle SET rent = 1 WHERE plate LIKE ? AND rent = 0";
    private String RETURN_VEHICLE_SQL = "UPDATE tvehicle SET rent = 0 WHERE plate LIKE ? AND rent = 1";

    private String RENT_UPDATE_USER_SQL = "UPDATE tuser SET rentedplate = ? WHERE login LIKE ? AND rentedplate IS NULL";
    private String RETURN_UPDATE_USER_SQL = "UPDATE tuser SET rentedplate = NULL WHERE login LIKE ? AND rentedplate IS NOT NULL";

    private String ADD_VEHICLE_SQL = "INSERT INTO tvehicle (plate, brand, model, year, price, category, rent) VALUES (?,?,?,?,?,?,?)";
    private String REMOVE_VEHICLE_SQL = "WITH deleted_vehicle AS ( DELETE FROM tvehicle WHERE plate = ? RETURNING plate ) UPDATE tuser SET rentedPlate = NULL WHERE rentedPlate IN (SELECT plate FROM deleted_vehicle)";


    public static JdbcVehicleRepository getInstance(){
        if (JdbcVehicleRepository.instance==null){
            instance = new JdbcVehicleRepository();
        }
        return instance;
    }
    private JdbcVehicleRepository() {
        this.databaseManager = DatabaseManager.getInstance();
    }


    @Override
    public boolean rentVehicle(String plate, String login) {
        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = databaseManager.getConnection();
            conn.setAutoCommit(false); // reczny commit

            rentCarStmt = conn.prepareStatement(RENT_VEHICLE_SQL);
            rentCarStmt.setString(1, plate);
            int changed1 =rentCarStmt.executeUpdate();

            updateUserStmt = conn.prepareStatement(RENT_UPDATE_USER_SQL);
            updateUserStmt.setString(1, plate);
            updateUserStmt.setString(2, login);
            int changed2 =updateUserStmt.executeUpdate();

            if (changed1 > 0 && changed2 > 0) {
                System.out.println("wypozyczono");
                conn.commit();
                return true;
            } else {
                System.out.println("Nie wypożyczono");
                conn.rollback(); // wycofuje zmiany
            }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (rentCarStmt != null)
                    rentCarStmt.close();
                if (updateUserStmt != null)
                    updateUserStmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean returnVehicle(String plate, String login) {
        Connection conn = null;
        PreparedStatement rentCarStmt = null;
        PreparedStatement updateUserStmt = null;

        try {
            conn = databaseManager.getConnection();
            conn.setAutoCommit(false); // reczny commit

            rentCarStmt = conn.prepareStatement(RETURN_VEHICLE_SQL);
            rentCarStmt.setString(1, plate);
            int changed1 = rentCarStmt.executeUpdate();

            updateUserStmt = conn.prepareStatement(RETURN_UPDATE_USER_SQL);
            updateUserStmt.setString(1, login);
            int changed2 = updateUserStmt.executeUpdate();

            if (changed1 > 0 && changed2 > 0) {
                System.out.println("Vehicle returned.");
                conn.commit();
                return true;
            } else {
                System.out.println("Failed to return a vehicle.");
                conn.rollback(); // wycofuje zmiany
            }

        } catch(Exception e) {
            e.printStackTrace();
            if (conn!= null) {
                try {
                    conn.rollback(); // Wycofuje zmiany
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            try {
                if (rentCarStmt != null)
                    rentCarStmt.close();
                if (updateUserStmt != null)
                    updateUserStmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    @Override
    public boolean addVehicle(Vehicle vehicle) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try{
            connection = databaseManager.getConnection();
            connection.createStatement();
            preparedStatement = connection.prepareStatement(ADD_VEHICLE_SQL);

            preparedStatement.setString(1, vehicle.getPlate());
            preparedStatement.setString(2, vehicle.getBrand());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setDouble(4, vehicle.getYear());
            preparedStatement.setDouble(5, vehicle.getPrice());
            preparedStatement.setInt(7, vehicle.getRent());

            if (vehicle instanceof Motorcycle) {
                preparedStatement.setString(6, ((Motorcycle) vehicle).getCategory());
            } else {
                preparedStatement.setNull(6, Types.VARCHAR); // Or set a default category if applicable
            }

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle added successfully.");
                return true;
            }
            else {
                System.out.println("Failed to add vehicle.");
                return false;
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
    public boolean removeVehicle(String plate) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = databaseManager.getConnection();
            preparedStatement = connection.prepareStatement(REMOVE_VEHICLE_SQL);
            preparedStatement.setString(1, plate);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Vehicle removed successfully.");
                return true;
            } else {
                System.out.println("No Vehicle matching the plate numbers.");
                return false;
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
    @Override
    public Vehicle getVehicle(String vehiclePlate) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = databaseManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(GET_VEHICLE_SQL);
            preparedStatement.setString(1, vehiclePlate);
            rs = preparedStatement.executeQuery();
            if (rs.next()){
                Vehicle vehicle;
                String category = rs.getString("category");
                String plate = rs.getString("plate");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                int rent = rs.getInt("rent");
                if (category!=null){
                    //Motorcycle(String brand, String model, int year, double price, String plate, String category)
                    vehicle = new Motorcycle(brand, model, year, price, plate, category);

                }else{
                    vehicle = new Car(brand,model,year,price,plate);
                }
                vehicle.setRent(rent);
                return vehicle;
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;

    }
    @Override
    public Collection<Vehicle> getVehicles() {

        Collection<Vehicle> vehicles = new ArrayList<>();
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = databaseManager.getConnection();
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(GET_ALL_VEHICLE_SQL);
            while(rs.next()){
                Vehicle v;
                String category = rs.getString("category");
                String plate = rs.getString("plate");
                String brand = rs.getString("brand");
                String model = rs.getString("model");
                int year = rs.getInt("year");
                double price = rs.getDouble("price");
                int rent = rs.getInt("rent");
                if (category!=null){
                    //Motorcycle(String brand, String model, int year, double price, String plate, String category)
                    v = new Motorcycle(brand, model, year, price, plate, category);

                }else{
                    v = new Car(brand,model,year,price,plate);
                }
                v.setRent(rent);
                vehicles.add(v);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vehicles;

    }

}