package org.example;

import java.security.MessageDigest;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String dbVehicles = "dbvehicles.txt";
        String dbUsers = "dbusers.txt";

        IUserRepositoryImpl userRepository = new IUserRepositoryImpl(dbUsers);
        IVehicleRepositoryImpl vehicleRepository = new IVehicleRepositoryImpl(dbVehicles);


//        User client1 = new User("client1", hashString("123"), 1);
//        User client2 = new User("client2", hashString("489"), 1);
//        User admin1 = new User("admin1", hashString("789"), 0);
//        userRepository.addUser(client1);
//        userRepository.addUser(client2);
//        userRepository.addUser(admin1);
//        userRepository.save();

        userRepository.load();
        vehicleRepository.load();


//        Car car1 = new Car("Toyota", "Camry", 2020, 25000.00, false);
//        Car car2 = new Car("Honda", "Civic", 2018, 20000.00, false);
//
//        Motorcycle motorcycle1 = new Motorcycle("Harley-Davidson", "Sportster", 2021, 12000.00, false, "Sport");
//        Motorcycle motorcycle2 = new Motorcycle("BMW", "S1000RR", 2019, 18000.00, false, "Sport");
//
//        iVehicleRepository.addVehicle(car1);
//        iVehicleRepository.addVehicle(car2);
//        iVehicleRepository.addVehicle(motorcycle1);
//        iVehicleRepository.addVehicle(motorcycle2);
//
//        iVehicleRepository.save();



        User currentUser = signIn(scanner, userRepository);

        if (currentUser.getRole() == 0)
            adminPanel(vehicleRepository, userRepository, scanner);
        else if (currentUser.getRole() == 1)
            clientPanel(currentUser, vehicleRepository, userRepository, scanner);



    }

    public static void clearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    public static User signIn(Scanner scanner, IUserRepositoryImpl userRepository) {
        String username = "";
        String password = "";
        User currentUser = null;

        while (currentUser == null){
            System.out.println("==============================");
            System.out.print("login: ");
            username = scanner.nextLine();
            System.out.print("password: ");
            password = scanner.nextLine();

            currentUser = Authenticator.checkpass(username, password, userRepository);
            clearScreen();
            if (currentUser == null)
                System.out.println("Account not found.");
        }

        return currentUser;
    }

    public static void adminPanel(IVehicleRepositoryImpl iVehicleRepositoryImpl, IUserRepositoryImpl iUserRepositoryImpl, Scanner scanner){

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
            int option1 = scanner.nextInt();
            clearScreen();
            switch (option1) {
                case 1:
                    for (Vehicle vehicle : iVehicleRepositoryImpl.getVehicles()) {
                        System.out.println(vehicle.toString());
                    }
                    break;
                case 2:
                    for (User user : iUserRepositoryImpl.getUsers()) {
                        System.out.println(iUserRepositoryImpl.clientProfileInfo(user));
                        // Vehicle rentedVehicle = iVehicleRepositoryImpl.getVehicle(user.getRentedId());
                        System.out.println("Rented vehicle:\n" + iVehicleRepositoryImpl.getVehicle(user.getRentedId()));
                    }
                    break;
                case 3:
                    System.out.println(
                            """
                            ==============================
                            [1] - Add car
                            [2] - Add motorcycle
                            ==============================
                            Type [1-2]:\s"""
                    );
                    int option2 = scanner.nextInt();
                    scanner.nextLine();
                    clearScreen();
                    String brand, model, category;
                    int year;
                    double price;

                    System.out.print("Brand: ");
                    brand = scanner.nextLine();
                    System.out.print("Model: ");
                    model = scanner.nextLine();
                    System.out.print("Year: ");
                    year = scanner.nextInt();
                    System.out.print("Price: ");
                    price = scanner.nextDouble();
                    scanner.nextLine();

                    if (option2 == 1) {
                        iVehicleRepositoryImpl.addVehicle(new Car(brand, model, year, price, false));
                    } else if (option2 == 2) {
                        System.out.print("Category: ");
                        category = scanner.nextLine();
                        iVehicleRepositoryImpl.addVehicle(new Motorcycle(brand, model, year, price, false, category));
                    }
                    clearScreen();
                    System.out.println("Vehicle added successfully!");
                    break;
                case 4:
                    boolean available = false;
                    for (Vehicle vehicle : iVehicleRepositoryImpl.getVehicles()) {
                        if (!vehicle.rented) {
                            available = true;
                            System.out.println(vehicle + "- ID: " + vehicle.id);
                        }
                    }
                    if (available) {
                        System.out.print("Type ID: ");
                        int inputID = scanner.nextInt();
                        clearScreen();
                        if(iVehicleRepositoryImpl.removeVehicle(inputID)){
                            System.out.println("Vehicle is removed.");
                        }
                        else {
                            System.out.println("Invalid ID, try again.");
                        }
                    }
                    else {
                        clearScreen();
                        System.out.println("No available vehicle to remove, try later.");
                    }

                    break;
                case 5:
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

    public static void clientPanel(User user, IVehicleRepositoryImpl iVehicleRepositoryImpl, IUserRepositoryImpl iUserRepository, Scanner scanner){
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
            int option1 = scanner.nextInt();
            clearScreen();
            switch (option1) {
                case 1:
                    System.out.println(iUserRepository.clientProfileInfo(user));
                    // Vehicle rentedVehicle = iVehicleRepositoryImpl.getVehicle(user.getRentedId());
                    System.out.println("Rented vehicle:\n" + iVehicleRepositoryImpl.getVehicle(user.getRentedId()));
                    break;
                case 2:
                    if(user.getRentedId() == -1) {
                        clearScreen();
                        System.out.println("You don't rent any vehicle at the moment.");
                        break;
                    }
                    if (iVehicleRepositoryImpl.returnVehicle(user.getRentedId()) && iUserRepository.rentVehicle(user.id, -1)){
                        clearScreen();
                        System.out.println("Vehicle returned.");
                    }

                    break;
                case 3:
                    if (user.getRentedId() != -1) {
                        clearScreen();
                        System.out.println("You have already rented a vehicle");
                        break;
                    }

                    boolean available = false;
                    for (Vehicle vehicle : iVehicleRepositoryImpl.getVehicles()) {
                        if (!vehicle.rented) {
                            available = true;
                            System.out.println(vehicle + "- ID: " + vehicle.id);
                        }
                    }
                    if (available) {
                        System.out.print("Type ID: ");
                        int inputID = scanner.nextInt();
                        clearScreen();
                        if(iVehicleRepositoryImpl.rentVehicle(inputID) && iUserRepository.rentVehicle(user.id, inputID)){
                            System.out.println("Vehicle is rented.");
                        }
                        else {
                            System.out.println("Invalid ID, try again.");
                        }
                    }
                    else {
                        clearScreen();
                        System.out.println("No available vehicle to rent, try later.");
                    }

                    break;
                case 4:
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