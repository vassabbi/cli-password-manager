package com.example.passwordManager.Controller;

import java.util.List;
import java.util.Scanner;

import com.example.passwordManager.Model.Entry;
import com.example.passwordManager.Service.VaultService;

public class VaultController {

    private final Scanner scanner = new Scanner(System.in);
    private final VaultService vaultService = new VaultService();

    public VaultController() {
        
    }

    public void startCLICycle() {
        System.out.println("Welcome to the Password Manager CLI!");
        boolean running = true;
        while (running) {
            System.out.println("---------------------------------------------------------");
            System.out.println("Please choose an option:");
            System.out.println("add - Add Entry");
            System.out.println("list - View Entries");
            System.out.println("show <id> - View Entry with ID");
            System.out.println("search <keyword> - Search Entries");
            System.out.println("remove <id> - Remove Entry with ID");
            System.out.println("update <id> - Update Entry with ID");
            System.out.println("backup - Backup Vault");
            System.out.println("restore - Restore Vault from Backup");
            System.out.print("exit - Exit\n> ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "add" -> addEntry();
                case "list" -> viewEntries();
                case "show" -> viewEntryById();
                case "exit" -> {
                    saveEntries();
                    running = false;
                    System.out.println("Exiting Password Manager CLI. Goodbye!");
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void addEntry() {
        System.out.println("Enter service name:");
        String serviceName = scanner.nextLine();
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        System.out.println("Enter notes (optional):");
        String notes = scanner.nextLine();
        System.out.println("Enter URL (optional):");
        String url = scanner.nextLine();
        if (vaultService.addEntry(serviceName, username, password, notes, url)){
            System.out.println("Entry added");
        } else {
            System.out.println("Duplicated service name and username");
        }
        // Implementation for adding an entry
    }

    private void viewEntries() {
        List<Entry> entries= vaultService.getAllEntries();
        String formatString = "%-9s %-15s %-15s %-15s %-25s %-40s%n";
        System.out.printf(formatString, "ID", "Service name", "Login", "Password", "URL", "Notes");
        for (var en : entries){
            System.out.printf(formatString,
                en.getId(),
                en.getServiceName(),
                en.getUsername(),
                en.getPassword(),
                en.getUrl(),
                en.getNotes()
            );
        }
    }

    private void viewEntryById() {
        System.out.println("Viewing entry by ID...");
        // Implementation for viewing an entry by ID
    }

    private void saveEntries() {
        vaultService.saveVault();
    }
}
