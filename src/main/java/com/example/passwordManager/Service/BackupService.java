package com.example.passwordManager.Service;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.example.passwordManager.Repository.VaultRepository;

public class BackupService {

    private final VaultRepository repo;
    private final String userName;

    public BackupService(VaultRepository repo, String username) {
        this.repo = repo;
        this.userName = username;
    }

    public boolean backup(){
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        Path originalPath = repo.getVaultPath(userName);
        Path backupPath = repo.createBackupPath(userName, timestamp);
        return repo.copy(originalPath, backupPath, false);
    }

    public List<Path> getBackups(){
        List<Path> backups = repo.listFiles();
        if (backups == null){
            return null;
        }
        List<Path> backupsFiltered = new ArrayList<>();
        for (Path backup: backups){
            String fileName = backup.getFileName().toString();
            if (fileName.startsWith(userName + "_") && fileName.endsWith(".vault.bak")){
                backupsFiltered.add(backup);
            }
        }
        return backupsFiltered;
    }

    public boolean restore(Path backupPath){
        Path originalPath = repo.getVaultPath(userName);
        return repo.copy(backupPath, originalPath, true);
    }
}
