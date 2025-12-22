package com.example.passwordManager;

import java.util.Scanner;

import com.example.passwordManager.Controller.AuthController;
import com.example.passwordManager.Controller.VaultController;
import com.example.passwordManager.Model.UserSession;
import com.example.passwordManager.Repository.VaultRepository;
import com.example.passwordManager.Service.AuthService;
import com.example.passwordManager.Service.BackupService;
import com.example.passwordManager.Service.EntryMetadata;
import com.example.passwordManager.Service.VaultApplicationService;
import com.example.passwordManager.Service.VaultCodec;
import com.example.passwordManager.Service.VaultService;

public class ApplicationRunner {

    private final Scanner scanner = new Scanner(System.in);

    private final VaultRepository vaultRepository = new VaultRepository();
    private final VaultCodec vaultCodec = new VaultCodec();
    private final EntryMetadata entryMetadata = new EntryMetadata();
    BackupService backupService = new BackupService(vaultRepository);

    public void run() {
        while (true) {
            UserSession session = authenticate();
            while (session == null) {
                session = authenticate();
            }
            runUserSession(session);
            System.out.println("Do you want to switch user? (y/n)");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("y")) {
                break;
            }
        }
    }

    private UserSession authenticate() {
        AuthService authService = new AuthService(vaultRepository, vaultCodec);
        AuthController authController = new AuthController(authService, scanner, backupService, vaultCodec);

        return authController.authFlow();
    }

    private void runUserSession(UserSession session) {
        VaultService vaultService = new VaultService(
                vaultRepository,
                vaultCodec,
                session.getUsername(),
                session.getPassword()
        );
        vaultService.loadVault();
        VaultApplicationService appService = 
                new VaultApplicationService(vaultService, backupService, entryMetadata);
        VaultController controller = 
                new VaultController(appService, scanner, session.getUsername());
        controller.startCLICycle();
        vaultService.saveVault();
    }
}
