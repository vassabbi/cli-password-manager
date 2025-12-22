package com.example.passwordManager.Utils;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class BackupUtils {
    public static Path chooseBackup(Scanner scanner, List<Path> backups) {
        if (backups == null || backups.isEmpty()) {
            System.out.println("No backups found");
            return null;
        }

        System.out.println("Choose backup:");
        for (int i = 0; i < backups.size(); i++) {
            System.out.printf("%-4s %-40s%n", (i + 1), backups.get(i).getFileName().toString());
        }
        System.out.printf("%-4s %-40s%n", 0, "back");

        String strPos = scanner.nextLine();
        Integer id = InputUtils.parseIntOrNull(strPos);

        if (id == null || id == 0 || id < 0 || id > backups.size()) {
            System.out.println(id == null ? "Id is not a number" : "Id is out of range / canceled");
            return null;
        }

        return backups.get(id - 1);
    }
}
