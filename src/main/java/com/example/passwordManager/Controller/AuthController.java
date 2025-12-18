package com.example.passwordManager.Controller;

import java.util.Scanner;

import com.example.passwordManager.Model.UserSession;
import com.example.passwordManager.Service.AuthService;

public class AuthController {
    private final AuthService authService;
    private final Scanner scanner = new Scanner(System.in);

    public AuthController(AuthService authService){
        this.authService = authService;
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
}
