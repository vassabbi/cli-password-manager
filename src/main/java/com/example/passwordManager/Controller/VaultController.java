package com.example.passwordManager.Controller;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

import com.example.passwordManager.Model.Entry;
import com.example.passwordManager.Service.VaultService;
import com.example.passwordManager.Utils.InputUtils;

public class VaultController {

    private final String formatString = "%-9s %-15s %-15s %-15s %-25s %-40s%n";
    private final Scanner scanner = new Scanner(System.in);
    private final VaultService vaultService = new VaultService();

    public VaultController() {
        
    }

    private void printEntries(List<Entry> entries){
        if (entries.isEmpty()){
            System.out.println("The list of entries is empty");
            return;
        }
        System.out.printf(formatString, "ID", "Service name", "Login", "Password", "URL", "Notes");
        for (var en : entries){
            printEntry(en);
        }
    }

    private void printEntry(Entry entry){
        System.out.printf(formatString,
            entry.getId(),
            entry.getServiceName(),
            entry.getUsername(),
            entry.getPassword(),
            entry.getUrl(),
            entry.getNotes()
        );
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
            System.out.println("---------------------------------------------------------");
            String choice = scanner.nextLine();
            String[] parts = choice.split("\\s+", 2);
            String command = parts[0];
            String args = parts.length > 1 ? parts[1] : "";
            switch (command) {
                case "add" -> addEntry();
                case "list" -> viewAllEntries();
                case "show" -> viewEntryById(args);
                case "search" -> searchByKeyword(args);
                case "remove" -> removeById(args);
                case "update" -> updateById(args);
                case "backup" -> makeBackup();
                case "restore" -> restore();
                case "exit" -> {
                    vaultService.saveVault();
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
        vaultService.saveVault();
    }

    private void viewAllEntries() {
        List<Entry> entries = vaultService.getAllEntries();
        printEntries(entries);
    }

    private void viewEntryById(String args) {
        args = args.trim();
        Integer id = InputUtils.parseIntOrNull(args);
        if (id == null){
            System.out.println("Id is not a number");
            return;
        }
        Entry entry = vaultService.getEntryById(id);
        if (entry == null){
            System.out.println("An Entry with this id was not found");
        } else {
            printEntry(entry);
        }
    }

    private void searchByKeyword(String args) {
        args = args.trim();
        if (args.length() < 2){
            System.out.println("Keyword must be at least 2 characters");
            return;
        }
        List<Entry> entries = vaultService.getEntriesByKeyword(args);
        printEntries(entries);
    }

    private void removeById(String args){
        args = args.trim();
        Integer id = InputUtils.parseIntOrNull(args);
        if (id == null){
            System.out.println("Id is not a number");
            return;
        }

        if (!vaultService.removeEntryById(id)){
            System.out.println("An Entry with this id was not found");
        } else {
            vaultService.saveVault();
            System.out.println("An entry was removed");
        }
    }

    private void updateById(String args){
        args = args.trim();
        Integer id = InputUtils.parseIntOrNull(args);
        if (id == null){
            System.out.println("Id is not a number");
            return;
        }

        Entry entry = vaultService.getEntryById(id);
        if (entry == null){
            System.out.println("An Entry with this id was not found");
            return;
        }
        for (String fieldName : vaultService.getEditableFieldNames()){
            System.out.println("Current " + fieldName + ":" + vaultService.getField(entry, fieldName));
            System.out.println("Enter a new " + fieldName + " (or press Enter to keep)");
            String newValue = scanner.nextLine();
            if (!newValue.equals("")){
                vaultService.updateField(entry, fieldName, newValue);
            }
        }
        vaultService.saveVault();
        System.out.println("The Entry has been updated");
    }

    private void makeBackup(){
        if (vaultService.backup()){
            System.out.println("Backup was created");
        } else {
            System.out.println("Failed to create backup");
        }
    }

    private void restore(){
        List<Path> backups = vaultService.getBackups();
        System.out.println("Choose backup");
        for (int i = 0; i < backups.size(); i++){
            System.out.printf("%-4s %-40s%n", (i + 1), backups.get(i).getFileName().toString());
        }
        System.out.printf("%-4s %-40s%n", 0, "back");
        String strPos = scanner.nextLine();
        Integer id = InputUtils.parseIntOrNull(strPos);

        if (id == null){
            System.out.println("Id is not a number");
            return;
        }

        if (id == 0){
            return;
        }

        if (id < 1 || id > backups.size()){
            System.out.println("Id is out of range");
            return;
        }

        id--;
        Path neededBackup = backups.get(id);
        if (!vaultService.restore(neededBackup)){
            System.out.println("Backup restore error");
        } else {
            System.out.println("The backup restore was successful");
        }
    }
}
