package com.example.passwordManager.Controller;

import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Scanner;

import com.example.passwordManager.Model.AuthResult;
import com.example.passwordManager.Model.UserSession;
import com.example.passwordManager.Service.AuthService;
import com.example.passwordManager.Service.BackupService;
import com.example.passwordManager.Service.VaultCodec;
import com.example.passwordManager.Utils.BackupUtils;

public class AuthController {
    private final AuthService authService;
    private final Scanner scanner;
    private final BackupService backupService;
    private final VaultCodec codec;

    public AuthController(AuthService authService, Scanner scanner, BackupService backupService, VaultCodec codec){
        this.authService = authService;
        this.scanner = scanner;
        this.backupService = backupService;
        this.codec = codec;
    }

    public UserSession login(){
        System.out.println("Welcome! Please log in.");
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Password: ");
        char[] password = scanner.nextLine().toCharArray();

        AuthResult result = authService.login(username, password);
        
        if (result == AuthResult.SUCCESS) {
            return new UserSession(username, password);
        } else {

            if (result == AuthResult.VAULT_NOT_FOUND) {
                System.out.println("Vault is not found");
            } else if (result == AuthResult.INVALID_PASSWORD_OR_CORRUPTED){
                System.out.println("Invalid password or vault is corrupted");
            }
            if (restoreFromBackup(username, password)){
                return new UserSession(username, password);
            }
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

    private boolean restoreFromBackup(String username, char[] password) {
        List<Path> backups = backupService.getBackups(username);
        if (backups.isEmpty()) {
            System.out.println("No backups found");
            return false;
        }
        System.out.println("Do you want to restore from a backup? (y/n)");
        String choice = scanner.nextLine().trim();
        if (!choice.equalsIgnoreCase("y")) return false;
        Path neededBackup = BackupUtils.chooseBackup(scanner, backups);
        if (neededBackup == null) return false;

        try {
            byte[] data = backupService.loadBackup(neededBackup);
            codec.decode(data, password);  // Если не удалось — выбросит исключение
        } catch (GeneralSecurityException e) {
            System.out.println("Cannot restore backup: incorrect password or corrupted file");
            return false;
        }
        if (backupService.restore(username, neededBackup)){
            System.out.println("The backup restore was successful");
            return true;
        } else {
            System.out.println("Backup restore error");
            return false;
        }
    }
}


