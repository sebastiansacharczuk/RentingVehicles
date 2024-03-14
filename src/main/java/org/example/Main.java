package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filename = "data.txt";
        IVehicleRepositoryImpl iVehicleRepositoryImpl = new IVehicleRepositoryImpl(filename);

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
//        iVehicleRepository.save("data.txt");

        iVehicleRepositoryImpl.load(filename);
        while (true){
            System.out.print(
                    """
                    ==============================
                    [1] - Display all vehicles
                    [2] - Add vehicle
                    [3] - Rent vehicle
                    [4] - Exit
                    ==============================
                    Type [1-4]:\s"""
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
                case 3:
                    boolean available = false;
                    for (Vehicle vehicle : iVehicleRepositoryImpl.getVehicles()) {
                        if (!vehicle.rented) {
                            available = true;
                            System.out.println(vehicle.toString() + "- ID: " + vehicle.id);
                        }
                    }
                    if (available) {
                        System.out.print("Type ID: ");
                        int inputID = scanner.nextInt();
                        clearScreen();
                        if(iVehicleRepositoryImpl.rentVehicle(inputID)){
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

    public static void clearScreen() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }
}