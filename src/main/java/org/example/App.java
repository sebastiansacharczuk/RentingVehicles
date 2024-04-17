package org.example;

import org.example.authenticate.Authenticator;
import org.example.configuration.HibernateUtil;
import org.example.dao.IUserRepository;
import org.example.dao.IVehicleRepository;
import org.example.dao.hibernate.UserDAO;
import org.example.dao.hibernate.VehicleDAO;
import org.example.model.Car;
import org.example.model.Motorcycle;
import org.example.model.User;
import org.example.model.Vehicle;

import java.util.Objects;
import java.util.Scanner;

public class App {
    public static  User user = null;
    private final Scanner scanner = new Scanner(System.in);
    private final IUserRepository iur = UserDAO.getInstance(HibernateUtil.getSessionFactory());
    private final IVehicleRepository ivr = new VehicleDAO(HibernateUtil.getSessionFactory());
    public void run(){
        user = guiLogin();
        if (user.getRole() == User.Role.USER){
            guiUser();
        }
        else if (user.getRole() == User.Role.ADMIN){
            guiAdmin();
        }
    }

    public static void clearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
    private User guiLogin() {
        String username = "";
        String password = "";
        User currentUser = null;

        while (currentUser == null){
            System.out.println("==============================");
            System.out.print("login: ");
            username = scanner.nextLine();
            System.out.print("password: ");
            password = scanner.nextLine();

            currentUser = Authenticator.login(username, password);
            clearScreen();
            if (currentUser == null)
                System.out.println("Account not found.");
        }

        return currentUser;
    }

    private void guiAdmin(){

        while (true){
            System.out.print(
                    """
                    ==============================
                    [1] - Display all vehicles
                    [2] - Display all users
                    [3] - Add vehicle
                    [4] - Remove vehicle
                    [5] - Exit
                    ==============================
                    Type [1-5]:\s"""
            );
            String mode = scanner.nextLine();
            clearScreen();
            switch (mode) {
                case "1":
                    for (Vehicle v : ivr.getVehicles()) {
                        System.out.println(v);
                    }
                    break;
                case "2":
                    for (User u: iur.getUsers()) {
                        System.out.println(u);
                    }
                    break;
                case "3":
                    System.out.println(
                            """
                            ==============================
                            [1] - Add car
                            [2] - Add motorcycle
                            ==============================
                            Type [1-2]:\s"""
                    );
                    String option2 = scanner.nextLine();
                    clearScreen();
                    String brand, model, category, plate;
                    int year;
                    double price;

                    System.out.print("Brand: ");
                    brand = scanner.nextLine();
                    System.out.print("Model: ");
                    model = scanner.nextLine();
                    System.out.print("Year: ");
                    year = Integer.parseInt(scanner.nextLine());
                    System.out.print("Plate: ");
                    plate = scanner.nextLine();
                    System.out.print("Price: ");
                    price = Double.parseDouble(scanner.nextLine());


                    if (Objects.equals(option2, "1")) {
                        ivr.addVehicle(new Car(brand, model, year, price, plate));
                    } else if (Objects.equals(option2, "2")) {
                        System.out.print("Category: ");
                        category = scanner.nextLine();
                        ivr.addVehicle(new Motorcycle(brand, model, year, price, plate, category));
                    }
                    clearScreen();
                    System.out.println("Vehicle added successfully!");
                    break;
                case "4":
                    boolean available = false;
                    for (Vehicle vehicle : ivr.getVehicles()) {
                        if (!vehicle.getRent()) {
                            available = true;
                            System.out.println(vehicle);
                        }
                    }
                    if (available) {
                        System.out.print("Type plate numbers: ");
                        String inputPlate = scanner.nextLine();
                        clearScreen();
                        if(ivr.removeVehicle(inputPlate)){
                            System.out.println("Vehicle is removed.");
                        }
                        else {
                            System.out.println("Invalid plate numbers, try again.");
                        }
                    }
                    else {
                        clearScreen();
                        System.out.println("No available vehicle to remove, try later.");
                    }

                    break;
                case "5":
                    clearScreen();
                    System.out.println("Exit.");
                    return;
                default:
                    clearScreen();
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }

    private void guiUser(){
        while (true){
            System.out.print(
                    """
                    ==============================
                    [1] - Your Profile info
                    [2] - Return vehicle
                    [3] - Rent vehicle
                    [4] - Exit
                    ==============================
                    Type [1-4]:\s"""
            );
            String option1 = scanner.nextLine();
            clearScreen();
            switch (option1) {
                case "1":
                    System.out.println(iur.getUser(user.getLogin()));
                    break;
                case "2":
                    if(user.getVehicle() == null) {
                        clearScreen();
                        System.out.println("You don't rent any vehicle at the moment.");
                        break;
                    }
                    if (ivr.returnVehicle(user.getVehicle().getPlate(), user.getLogin())){
                        clearScreen();
                        System.out.println("Vehicle returned.");
                    }

                    break;
                case "3":
                    if (user.getVehicle() != null) {
                        clearScreen();
                        System.out.println("You have already rented a vehicle");
                        break;
                    }

                    boolean available = false;
                    for (Vehicle vehicle : ivr.getVehicles()) {
                        if (!vehicle.getRent()) {
                            available = true;
                            System.out.println(vehicle);
                        }
                    }
                    if (available) {
                        System.out.print("Type plate: ");
                        String inputPlate = scanner.nextLine();
                        clearScreen();
                        if(ivr.rentVehicle(inputPlate, user.getLogin())){
                            System.out.println("Vehicle is rented.");
                        }
                        else {
                            System.out.println("Invalid plate numbers, try again.");
                        }
                    }
                    else {
                        clearScreen();
                        System.out.println("No available vehicle to rent, try later.");
                    }

                    break;
                case "4":
                    clearScreen();
                    System.out.println("Exit.");
                    return;
                default:
                    clearScreen();
                    System.out.println("Invalid input.");
                    break;
            }
        }
    }


}
