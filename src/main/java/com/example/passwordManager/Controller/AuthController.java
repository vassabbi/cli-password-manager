package com.example.passwordManager.Controller;

import java.util.Scanner;

import com.example.passwordManager.Model.UserSession;
import com.example.passwordManager.Service.AuthService;

public class AuthController {
    private final AuthService authService;
    private final Scanner scanner;

    public AuthController(AuthService authService, Scanner scanner){
        this.authService = authService;
        this.scanner = scanner;
    }

    public UserSession login(){
        System.out.println("Welcome! Please log in.");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        char[] password = scanner.nextLine().toCharArray();


        if (authService.login(username, password)) {
            return new UserSession(username, password);
        } else {
            System.out.println("Invalid username or password!");
            return null;
        }
    }

    private UserSession register(){
        System.out.println("New username:");
        String username = scanner.nextLine().trim();

        System.out.println("New password:");
        char[] password = scanner.nextLine().toCharArray();

        if (authService.register(username, password)){
            System.out.println("User registered successfully");
            return new UserSession(username, password);
        } else {
            System.out.println("User already exists");
            return null;
        }
    }

    public UserSession authFlow(){
        while (true){
            System.out.println("---------------------------------------------------------");
            System.out.println("Please choose an option:");
            System.out.println("login - Sign in");
            System.out.println("register - Register a user");
            System.out.println("---------------------------------------------------------");
            String choice = scanner.nextLine();
            switch (choice) {
                case "login" -> {
                    return login();
                }
                case "register" -> register();
                default -> System.out.println("Invalid option. Please try again");
            }
        }
    }
    
}
